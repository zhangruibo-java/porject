package com.mr.service;



import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.bo.SearchBo;
import com.mr.bo.SearchResult;
import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodClient;
import com.mr.client.SpecClient;
import com.mr.common.utils.JsonUtils;
import com.mr.common.utils.PageResult;
import com.mr.dao.GoodRepository;
import com.mr.pojo.*;
import com.mr.util.HighLightUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
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

    /**
     * 查询
     * @param searchBo
     * @return
     */
    public PageResult<Goods> searchGood(SearchBo searchBo) {
//执行查询
        //构造查询条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
       //==================================查询商品======================================
        if(StringUtils.isNotEmpty(searchBo.getKey())){//判断查询条件是否为空
            builder.withQuery(
                    QueryBuilders.matchQuery("all",searchBo.getKey())
            );
        }
        //判断是否有过滤条件
        Map<String,String> filterMap = searchBo.getFilter();
       if( filterMap!=null &&  filterMap.size()!=0){

           //拼接过滤条件,循环map
           Set<String> filterKey= filterMap.keySet();
           //创建boll查询 ,方便循环内拼接
           BoolQueryBuilder boolQueryBuilder =QueryBuilders.boolQuery();
           //循环所有的sku

           filterKey.forEach(key->{
               //增加过滤条件 key? cpu cid3 ....  value? 小龙 76...
               if(key.equals("cid3") || key.equals("brandId")){
                   boolQueryBuilder.must(QueryBuilders.termQuery(key,filterMap.get(key)));
               }else{
                   boolQueryBuilder.must(QueryBuilders.termQuery("specs."+key+".keyword",filterMap.get(key)));
               }
           });
           builder.withFilter(boolQueryBuilder);
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
        //==================================查询商品======================================

        //==================================查询规格======================================
        //汇总所有商品下的分类规格
        builder.addAggregation(AggregationBuilders.terms("cate_gro").field("cid3"));
        //汇总所有商品下的品牌
        builder.addAggregation(AggregationBuilders.terms("brand_gro").field("brandId"));
        //查询得到聚合结果
        AggregatedPage<Goods> aggPage= (AggregatedPage<Goods>) goodRepository.search(builder.build());
        //获得分类结果
        LongTerms cateTerms= (LongTerms) aggPage.getAggregation("cate_gro");
        //获得聚合后的"桶" 数据

        List<LongTerms.Bucket> cateBuckets =cateTerms.getBuckets();
        /*cateBuckets.forEach(bucket -> {
            System.out.println("聚合后的分类数据"+bucket.getKeyAsNumber().longValue());
        });*/
        //获得分类集合
        List<Long> cateIds= cateBuckets.stream().map(bucket -> {
            return bucket.getKeyAsNumber().longValue();
        }).collect(Collectors.toList());
        //批量查询分类
        List<Category> categoryList= categoryClient.queryCategoryList(cateIds);


        //获得品牌聚合结果
        LongTerms brandTerms= (LongTerms) aggPage.getAggregation("brand_gro");
        //获得聚合后的"桶" 数据
        List<LongTerms.Bucket> brandBucket =brandTerms.getBuckets();
        //根据id查询出品牌详情
       /* brandBucket.forEach(bucket -> {
            System.out.println("聚合后的品牌数据"+bucket.getKeyAsNumber().longValue());
        });*/
       //根据id查询品牌数据
        //的到品牌集合
       List<Brand> brandList= brandBucket.stream().map(bucket -> {
          return  brandClient.queryBidById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());

       //汇总出热度最高的分类
        long maxDoc = 0;
        long maxCid = 0;

        for (LongTerms.Bucket  bucket : cateBuckets) {
            if(bucket.getDocCount() > maxDoc){
                maxDoc=bucket.getDocCount();
                maxCid=bucket.getKeyAsNumber().longValue();
            }
        }
        System.out.println("商品最多的分类是"+maxDoc);
        //根据分类查询出该分类下的查询规格
        //封装一个方法,查询规格从mysql 查询规格值 从es
        List<Map<String ,Object>> specMapList=getSpecMapList(maxCid,searchBo);
        //==================================查询规格======================================


        //分页数据
        SearchResult<Goods> pageResult = new SearchResult<>();
        pageResult.setTotal(goodsPage.getTotalElements());//总条数
        long total =goodsPage.getTotalElements();
        long size = searchBo.getSize();
        pageResult.setTotalPage(total % size == 0 ? total/size : total/size+1);//总页数
        //pageResult.setTotalPage((long)goodsPage.getTotalPages());
        pageResult.setItems(goodsPage.getContent());//当前页数据
        //将分类品牌集合设置到返回集合中
        pageResult.setBrandList(brandList);
        pageResult.setCategoryList(categoryList);
        //设置规格查询条件
        pageResult.setSpecMapList(specMapList);
       return pageResult;
    }

    /**
     * 返回组装好的规格参数
     * @param maxCid
     * @param searchBo
     * @return
     */
    private List<Map<String, Object>> getSpecMapList(long maxCid, SearchBo searchBo) {
        List<Map<String, Object>> specMapList= new ArrayList<>();

        //1查询规格名称
        List<SpecParam> specParamList=specClient.params(maxCid,true);
        //从es 查询规格的值
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //创建boll查询 ,方便循环内拼接
        BoolQueryBuilder boolQueryBuilder =QueryBuilders.boolQuery();

        //设置过滤条件
        Map<String,String> filterMap = searchBo.getFilter();
        //判断是否有过滤条件
        if( filterMap!=null &&  filterMap.size()!=0){
            System.out.println("设置规格中条件过滤");
            //拼接过滤条件,循环map
            Set<String> filterKey= filterMap.keySet();

            //循环所有的sku
            filterKey.forEach(key->{
                //增加过滤条件 key? cpu cid3 ....  value? 小龙 76...
                if(key.equals("cid3") || key.equals("brandId")){
                    boolQueryBuilder.must(QueryBuilders.termQuery(key,filterMap.get(key)));
                }else{
                    boolQueryBuilder.must(QueryBuilders.termQuery("specs."+key+".keyword",filterMap.get(key)));
                }
            });
            //聚合会在过滤之后执行吗?
          //  builder.withFilter(boolQueryBuilder);
        }
        //设置关键字
        if(StringUtils.isNotEmpty(searchBo.getKey())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("all",searchBo.getKey()));
            builder.withQuery(boolQueryBuilder);
        }
        //聚合规格的值
        //循环 加入 聚合条件
        specParamList.forEach(specParam -> {
            //规格名称
            String key = specParam.getName();
            builder.addAggregation(AggregationBuilders.terms(key).field("specs."+key+".keyword"));

        });
        //值进行查询,得到聚合结果
        AggregatedPage<Goods>  goodsPage= (AggregatedPage<Goods>) goodRepository.search(builder.build());
        specParamList.forEach(specParam -> {
            //组装map放入集合
            String key = specParam.getName();
         //规格名称作为key
            Map<String,Object> map=new HashMap<>();
            map.put("key",key);
            //聚合出的值作为value

            StringTerms stringTerms= (StringTerms) goodsPage.getAggregation(specParam.getName());
            //取出数据
            List<StringTerms.Bucket> bucketList = stringTerms.getBuckets();
            //转为 集合string类型
           List<String> values= bucketList.stream().map(bucket -> {
                return bucket.getKeyAsString();
            }).collect(Collectors.toList());
           map.put("values",values);
           //将map放入集合
            specMapList.add(map);
        });
        return  specMapList;
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

}
