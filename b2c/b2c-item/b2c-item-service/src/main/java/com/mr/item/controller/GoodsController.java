package com.mr.item.controller;

import com.mr.bo.SpuBo;
import com.mr.common.utils.PageResult;
import com.mr.item.service.BrandService;
import com.mr.item.service.GoodsService;
import com.mr.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    public GoodsService goodsService;
    @Autowired
    public BrandService brandService;

    /**
     * 查询商品分页
     * @param key 搜索关键字
     * @param page 当前页
     * @param rows 每页条数
     * @param sale 上下架 状态
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam("key") String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) String sale
    ) {
       PageResult<SpuBo> list = goodsService.querySpuByPage(key,page,rows,sale);
      /*  Long total = goodsService.getCount();
        PageResult pageResult = new PageResult();
        pageResult.setItems(list);
        pageResult.setTotal(total);*/
        return ResponseEntity.ok(list);
    }
    @PostMapping("addGoods")
    public ResponseEntity<Void> addGoods(@RequestBody SpuBo spuBo){
            //执行新增，新增完成之后发消息
        Long spuId=   goodsService.addGoods(spuBo);
           goodsService.sendSaveMessage(spuId);
            return ResponseEntity.ok(null);
        }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail>  queryDetail(@PathVariable("spuId") Long spuId){
        SpuDetail spuDetail=goodsService.queryDetail(spuId);
        return ResponseEntity.ok(spuDetail);
    }
    @GetMapping("querySku/{spuId}")
    public ResponseEntity<List<Sku>>  querySkuList(@PathVariable("spuId") Long spuId){
        List<Sku> list=goodsService.querySkuList(spuId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("updateGood")
    public ResponseEntity<List<SpuBo>> updateGoods(@RequestBody SpuBo spuBo){
        List<SpuBo> list= goodsService.updateGoods(spuBo);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据spuIdc查询spu
     * @param spuId
     * @return
     */
    @GetMapping("spu/{spuId}")
    public ResponseEntity<Spu> querySpuBySpuId(@PathVariable("spuId") Long spuId){
        Spu spu = goodsService.querySpuBySpuId(spuId);
        return ResponseEntity.ok(spu);
    }

    /**
     * 根据skuid查询sku
     * @param spuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> queryCartBySkuId(@PathVariable("skuId") Long spuId){
        Sku sku = goodsService.queryCartBySkuId(spuId);
        return ResponseEntity.ok(sku);
    }

    /**
     *修改库存
     * @param stock
     * @return
     */
    @PutMapping("updateStock")
    public ResponseEntity<Boolean> updateStock(@RequestBody Stock stock){
        //修改库存方法，必须给返回值 起码要提示 true or false
        Boolean  falg=  goodsService.updateStock(stock);
        return ResponseEntity.ok(falg);
    }
}

