package com.mr.cart.bo;

import lombok.Data;

/**
 * 接受前台参数 用于对用缓存中的增加查询等
 */
@Data
public class Cart {
    //从前台传参

    private Long userId;// 用户id
    private Long skuId;// 商品id
    private String title;// 标题
    private String image;// 图片
    private Long price;// 加入购物车时的价格
    private Integer num;// 购买数量
    private String ownSpec;// 商品规格参数

    //当sku数据发生变化 对比
    private Long oldPrice;//改变之前的价格
    private Integer stock;//库存
    private Boolean enable;//上下架
}
