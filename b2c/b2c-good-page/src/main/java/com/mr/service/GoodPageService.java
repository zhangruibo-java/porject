package com.mr.service;

import com.mr.fegin.BrandClient;
import com.mr.fegin.CategoryClient;
import com.mr.fegin.GoodClient;
import com.mr.fegin.SpecClient;
import com.mr.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodPageService {
    @Autowired
    private GoodClient goodClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecClient specClient;
    /**
     * 查询商品详情
     */
   /* public  Map<String,Object> getGoodInfo(Long spuId){
        //spu
        Spu spu = goodClient.querySpuBySpuId(spuId);
        //spu_detail
        SpuDetail spuDetail = goodClient.queryDetail(spuId);
        //sku集合
        List<Sku> skuList = goodClient.querySkuList(spuId);
        //分类
        List<Category> categoryList=categoryClient.queryCategoryList(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        //品牌
        Brand brand = brandClient.queryBidById(spu.getBrandId());
        //规格组
        List<SpecGroup> specGroupList=specClient.groups(spu.getCid3());
        //规格:将规格放入规格组中
        specGroupList.forEach(specGroup -> {
            //查询出规格,根据规格组id
           List<SpecParam> specParamList= specClient.querySpecParam(spu.getId(),null,null,null);
            specGroup.setSpecParamList(specParamList);
        });
        //特有规格参数
        List<SpecParam> specParamList=specClient.querySpecParam(null,spu.getCid3(),null,false);
    //方便页面数据展示 将list转为map
        Map<Long,String> specParamMap = new HashMap<>();
        specParamList.forEach(specParam -> {
            specParamMap.put(specParam.getId(),specParam.getName());
        });
        //将返回的数据放入map
        Map<String,Object>  modelMap = new HashMap();
        modelMap.put("spu",spu);
        modelMap.put("spuDetail",spuDetail);
        modelMap.put("skuList",skuList);
        modelMap.put("categoryList",categoryList);
        modelMap.put("brand",brand);
        modelMap.put("specGroupList",specGroupList);
        modelMap.put("specParamMap",specParamMap);

  return modelMap;
    }*/
    public Map<String,Object> getGoodInfo(Long spuId){
        //spu数据
        Spu spu= goodClient.querySpuBySpuId(spuId);
        //spu 详情
        SpuDetail spuDetail= goodClient.queryDetail(spuId);
        //skus
        List<Sku> skuList=goodClient.querySkuList(spuId);

        //分类
        List<Category> categoryList=categoryClient.queryCategoryList(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

        //品牌
        Brand brand=brandClient.queryBidById(spu.getBrandId());
        //规格组
        List<SpecGroup> specGroupList=specClient.groups(spu.getCid3());
        //规格组填充规格详细 需要在specGroup中加入specParam集合，加忽略注解
        specGroupList.forEach(specGroup -> {
            specGroup.setSpecParamList( specClient.querySpecParam(specGroup.getId(),null,null,null));
        });
        //特有规格参数
        List<SpecParam> specParamList=specClient.querySpecParam(null,spu.getCid3(),null,false);
        Map<Long,String> specParamMap=new HashMap();
        specParamList.forEach(specParam -> {
            specParamMap.put(specParam.getId(),specParam.getName());
        });
        //组装所有数据，返回页面
        Map<String,Object> modelMap =new HashMap<>();
        modelMap.put("spu",spu);
        modelMap.put("spuDetail",spuDetail);
        modelMap.put("skuList",skuList);
        modelMap.put("categoryList",categoryList);
        modelMap.put("brand",brand);
        modelMap.put("specGroupList",specGroupList);
        modelMap.put("specParamMap",specParamMap);
        return modelMap;
    }
}
