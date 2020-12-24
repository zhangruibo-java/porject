package com.mr.item.controller;

import com.mr.common.advice.ExceptionEnums;
import com.mr.common.advice.MrException;
import com.mr.item.pojo.Goods;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("goods")
public class ItemController {
    @PostMapping("addGoods")
    public ResponseEntity<Goods> addGoods(Goods goods){
        if(goods.getPrice() == null){

            System.out.println("参数异常");
            //return ResponseEntity.status(401).body("价格不能为空");
            //exception 异常的超类  runtime 运行时异常
            //  throw  new RuntimeException("参数异常");
            //自定义异常太多,需要集中管理,不能写死
            //使用枚举 可以传递多个参数(常量),一般不会修改
            throw new MrException(ExceptionEnums.PRICE_IS_NULL);
        }else {
            goods.setId(new Random().nextLong());
        }
        if (goods.getPrice() >50000){
            throw new MrException(ExceptionEnums.MYSQL_CONCTION_ERROR);
        }
        return ResponseEntity.status(201).body(goods);


    }
}
