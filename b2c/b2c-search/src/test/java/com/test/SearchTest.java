package com.test;

import com.mr.SearchApplication;
import com.mr.bo.SpuBo;
import com.mr.client.GoodClient;
import com.mr.common.utils.PageResult;
import com.mr.dao.GoodRepository;
import com.mr.pojo.Goods;
import com.mr.service.GoodService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class SearchTest {
    /*
    * xuqiu
    *  sku:价格几何 图片集合
    *  spu : 标题/id
    * */

    //创建索引库
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    //操作数据
    @Autowired
    private GoodRepository goodRepository;

    @Autowired
    private GoodService goodService;

    @Autowired
    private GoodClient goodClient;
    @Test
    public void searchTest(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
        System.out.println("ok");
    }
    //创建数据
    //通过item服务读取数据,填充到es数据库中
    @Test
    public void searchSave(){
        //根据spuId 2 把其他数据填充完毕,最后增加到es数据库

        Goods goods=goodService.getGoodsBySpuId(2L);
        goodRepository.save(goods);
    }

    @Test
    public void searchSave2(){
        //根据spuId 2 把其他数据填充完毕,最后增加到es数据库

        PageResult<SpuBo> pageResult = goodClient.querySpuByPage("",0,200,"true");
        pageResult.getItems().forEach(spuBo -> {
            Goods goods=goodService.getGoodsBySpuId(spuBo.getId());
            goodRepository.save(goods);
        });
    }
}
