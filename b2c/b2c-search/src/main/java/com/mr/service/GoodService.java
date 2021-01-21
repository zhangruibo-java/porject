package com.mr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.bo.SearchBo;
import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodClient;
import com.mr.client.SpecClient;
import com.mr.common.utils.JsonUtils;
import com.mr.common.utils.PageResult;
import com.mr.dao.GoodRepository;
import com.mr.pojo.*;
import com.mr.util.HighLightUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodService {
    /**
     * 根据spuId获得goods对象
     * @return
     */
    @Autowired
    private GoodClient goodClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecClient specClient;

    @Autowired
    private GoodRepository goodRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Goods getGoodsBySpuId(Long spuId){
        //商品数据 ,再mysql中分为 spu sku stock detail
        //通过feign 调用item服务 声明式接口调用 调用接口  ITEM-SERVICE
        Goods goods = new Goods();
        Spu spu = goodClient.querySpuBySpuId(spuId);
        //查询品牌名称
        Brand brand= brandClient.queryBidById(spu.getBrandId());
        //查询分类
        List<String> categoriesNameList= categoryClient.queryCategoryList(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()))
                .stream().map(category -> { return  category.getName();
        }).collect(Collectors.toList());
        //填充价格
        List<Long> priceList = new ArrayList<>();
        //填充sku : 需要什么数据就填充什么数据 id title 图片一张 价格[]
        List<Sku> skuList= goodClient.querySkuList(spuId);
        skuList= skuList.stream().map(sku -> {
           //Sku sku1 = new Sku();
           //图片只取一张
            sku. setCreateTime(null);
            sku. setSpuId(null);
            sku. setLastUpdateTime(null);
            sku.setStock(null);
            sku. setEnable(null);
            //将图片集合取一张图片就可以
            sku. setIndexes(null);
            sku. setOwnSpec(null);
            sku.setImages(StringUtils.isNotEmpty(sku.getImages())?sku.getImages().split(",")[0]:"");
           //填充价格
            priceList.add(sku.getPrice());
            return sku;
        }).collect(Collectors.toList());

        SpuDetail spuDetail= goodClient.queryDetail(spu.getId());
        //把通用规格变成对象  spuDetail.getGenericSpec();
        Map<Long,String> genSpecMap= JsonUtils.parseMap(spuDetail.getGenericSpec(),Long.class,String.class);

        //把特有规格变成对象  spuDetail.getSpecialSpec();
        Map<Long,List<String>> specialMap=
                JsonUtils.nativeRead(spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
                }) ;

        Map<String,Object> specMap = new HashMap<>();
        //填充可被搜素的规格属性,值{运行内存,屏幕尺寸,频率...} generic  searching:是否被检索
      List<SpecParam> specParamList=  specClient.params(spu.getCid3(),true);

      specParamList.forEach(specParam -> {
          //specParam.getId(): 取商品的规格值,需要根据id 从detail表去get获取
          Object value=null;//扩大作用域
        if (specParam.getGeneric()){//判断是否是通用规格
             value= genSpecMap.get(specParam.getId()); //通用
            //判断规格的值 是否是数字类型
            if(specParam.getNumeric()){
                //把文本变成区间
                value = this.chooseSegment(value.toString(),specParam);
            }
        }else{
            value= specialMap.get(specParam.getId()); //特有
        }
          specMap.put(specParam.getName(),value);
      });
        goods.setId(spu.getId());
        goods.setAll(spu.getTitle()+","+brand.getName()+","+ StringUtils.join(categoriesNameList,","));
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setPrice(priceList);//价格:一条商品集包含多个价格
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specMap);//填充规格

       return goods;
    }
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 查询
     * @param searchBo
     * @return
     */
    public PageResult<Goods> searchGood(SearchBo searchBo) {
//执行查询
        //构造查询条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        if(StringUtils.isNotEmpty(searchBo.getKey())){//判断查询条件是否为空
            builder.withQuery(
                    QueryBuilders.matchQuery("all",searchBo.getKey())
            );
        }
        //设置分页
        builder.withPageable(PageRequest.of(searchBo.getPage(),searchBo.getSize()));

        //查询
       Page<Goods> goodsPage= goodRepository.search(builder.build());
       //设置高亮关键字
        builder.withHighlightFields(new HighlightBuilder.Field("all")
                .preTags("<font color='red'>")
                .postTags("</font>"));
       Map<Long,String> hignMap= HighLightUtil.getHignLigntMap(elasticsearchTemplate,builder.build(),Goods.class,"all");
        goodsPage.getContent().forEach(goods -> {
            //高亮字段替换
            goods.setAll(hignMap.get(goods.getId()));
        });
        //分页数据
        PageResult<Goods> pageResult = new PageResult<>();
        pageResult.setTotal(goodsPage.getTotalElements());//总条数
        long total =goodsPage.getTotalElements();
        long size = searchBo.getSize();
        pageResult.setTotalPage(total % size == 0 ? total/size : total/size+1);//总页数
        //pageResult.setTotalPage((long)goodsPage.getTotalPages());
        pageResult.setItems(goodsPage.getContent());//当前页数据

       return pageResult;
    }
}
