package com.mr.order.bo;

import lombok.Data;

@Data
public class CartBo {
    //购买商品 价格信息  不需要从前台传
    private Integer num;
    private Long skuId;

}
