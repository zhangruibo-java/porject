package com.mr.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.bo.SpuBo;
import com.mr.common.utils.PageResult;
import com.mr.item.mapper.*;
import com.mr.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    public GoodsMapper goodsMapper;
    @Autowired
    public BrandMapper brandMapper;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    public CategoryMapper categoryMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 商品分页数据
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    public PageResult<SpuBo> querySpuByPage(String key, Integer page, Integer rows, String saleable) {

       /* if(rows == -1){
            rows = 200;
        }*/

        //分页工具类
        PageHelper.startPage(page,rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria=example.createCriteria();
        //查询 模糊查询
        if (StringUtils.isNotEmpty(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //判断上下架
        if (!("0").equals(saleable)){
            criteria.andEqualTo("saleable",Boolean.valueOf(saleable));
        }

        Page<Spu> spuPage= (Page<Spu>) goodsMapper.selectByExample(example);

        //填充, 分类,品牌名称
        //返回集合类型
        List<SpuBo> spuBoList = spuPage.getResult().stream().map(spu -> {
            //需要将 spu 赋值到SpuBo 需要额外增加分类 和 品牌
            SpuBo bo = new SpuBo();
            //使用 工具类 复制属性 将 pu对象 赋值到 bo中
            BeanUtils.copyProperties(spu,bo);

            //填充品牌名称
            bo.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());

            //填充分类
            //   List<String> names = categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

            // 将分类名称拼接后存入
            //  bo.setCategoryName(StringUtils.join(names, "/"));
            Category c1=categoryMapper.selectByPrimaryKey(bo.getCid1());
            Category c2=categoryMapper.selectByPrimaryKey(bo.getCid2());
            Category c3=categoryMapper.selectByPrimaryKey(bo.getCid3());
            bo.setCategoryName(c1.getName()+"/"+c2.getName()+"/"+c3.getName());

            /* List<Long> list =  Arrays.asList(bo.getCid1(),bo.getCid2(),bo.getCid3());*/
            /* categoryMapper.selectByPrimaryKey()*/

            return bo;
        }).collect(Collectors.toList());
        PageResult pageResult = new PageResult();
        //设置分页和总条数
        pageResult.setItems(spuBoList);
        pageResult.setTotal(spuPage.getTotal());

        return new PageResult<SpuBo>(pageResult.getTotal(), spuBoList);
    }
    @Transactional//事务
    public void addGoods(SpuBo spuBo) {
        System.out.println("111111111111");

        //----------------------以下是 保存 spu 数据---------------------------
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo,spu);//spuBo赋值给spu
        spu.setSaleable(true); //是否上架
        spu.setValid(true); //是否有效
        Date now= new Date();
        spu.setCreateTime(now); //创建时间,修改时间
        spu.setLastUpdateTime(now); //创建时间,修改时间
        spuMapper.insert(spu);//mapper 写sql语句
        //spuMapper.add(spu);
        //------------------ 以上是 保存 spu 数据--------------------------------------
        //----------------------以下是 保存 spuDetail 数据 (1条)---------------------------
        SpuDetail spuDetail = spuBo.getSpuDetail();//get 得到spuBo中封装的SpuDetail数据
        spuDetail.setSpuId(spu.getId());//自身没有主键自动递增
        spuDetailMapper.insert(spuDetail);
        //  spuDetailMapper.add(spuDetail);
        //   spuDetail.setSpuId(spu.getId());
        //  spuDetailMapper.insert(spuDetail);
        //------------------ 以上是 保存 spuDetail 数据--------------------------------------
        //----------------------以下是 保存 sku stock 数据 ---------------------------
        //List<Sku> skus=spuBo.getSkus();
        spuBo.getSkus().forEach(sku -> {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(now);
            sku.setLastUpdateTime(now);
            skuMapper.insert(sku);
            //  skuMapper.add(sku);

            Stock stock =new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
            // stockMapper.add(stock);
        });
        System.out.println(spu);
    }

    public SpuDetail queryDetail(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkuList(Long spuId) {
        //根据spu查询sku集合
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList=skuMapper.select(sku);
        skuList.forEach(sku1 -> {
            //填充库存
            sku1.setStock(stockMapper.selectByPrimaryKey(sku1.getId()).getStock());
        });
        return skuList;
    }
    @Transactional
    public List<SpuBo> updateGoods(SpuBo spuBo) {
        Spu spu= new Spu();//获取spu数据
        BeanUtils.copyProperties(spuBo,spu);
        Date now = new Date();//实例化时间
        spu.setLastUpdateTime(now);//修改时间
        spu.setId(spuBo.getId());
        spu.setSaleable(true); //是否上架
        spu.setValid(true); //是否有效
        spuMapper.updateByPrimaryKeySelective(spu);
        //-----
        SpuDetail spuDetail=spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.updateByPrimaryKey(spuDetail);
        //-----
        spuBo.getSkus().forEach(sku -> {
            if (sku.getId()==null){
                sku.setSpuId(spuBo.getId());
                sku.setCreateTime(now);
                sku.setLastUpdateTime(now);
                skuMapper.insert(sku);

                Stock stock =new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.insert(stock);
            }else {
                sku.setSpuId(spu.getId());
                /*sku.setCreateTime(now);*/
                sku.setLastUpdateTime(now);
                skuMapper.updateByPrimaryKeySelective(sku);

                Stock stock =new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.updateByPrimaryKeySelective(stock);
            }

            //  skuMapper.add(sku);

         /*   if (){

            }else {

            }*/

            //stockMapper.add(stock);
        });
        //-----

        return null;
    }

    public Spu querySpuBySpuId(Long spuId) {
        return goodsMapper.selectByPrimaryKey(spuId);
    }

   /* public List<Spu> querySpuByPage(String key, Integer page, Integer rows, String sale) {
        Integer page1 = (page - 1) * rows;
        if (StringUtils.isNotEmpty(key)) {
            return goodsMapper.querySpuByPage(key, page1, rows,sale);
        } else {
            return goodsMapper.querySpuPage(key, page1, rows,sale);
        }

    }
    public Long getCount () {
        return goodsMapper.getCount();
    }*/
}
