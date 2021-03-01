package com.mr.order.bo;

import lombok.Data;

import java.util.List;

//订单的业务对象
@Data
public class OrderBo {
    private Long addressId;//用户地址id
    private Integer invoiceType;//发票类型
    private String buyerMessage;//买家留言
    private Integer payMentType;//支付方式
    private List<CartBo> cartList;



}
