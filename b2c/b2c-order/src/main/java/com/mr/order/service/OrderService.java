package com.mr.order.service;


import com.mr.bo.UserInfo;

import com.mr.common.advice.ExceptionEnums;
import com.mr.common.advice.MrException;
import com.mr.common.utils.IdWorker;
import com.mr.order.bo.AddressBo;
import com.mr.order.bo.CartBo;
import com.mr.order.bo.OrderBo;
import com.mr.order.cleint.AddressClient;
import com.mr.order.cleint.GoodsClient;
import com.mr.order.mapper.OrderDetailMapper;
import com.mr.order.mapper.OrderMapper;
import com.mr.order.mapper.OrderStatusMapper;
import com.mr.order.pojo.Order;
import com.mr.order.pojo.OrderDetail;
import com.mr.order.pojo.OrderStatus;
import com.mr.pojo.Sku;
import com.mr.pojo.Stock;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private IdWorker idWorker;
    /**
     * 订单新增
     * @param orderBo
     * @param info
     */
    @Transactional
    public Long createOrder(OrderBo orderBo, UserInfo info) {
        //根据前端传来的参数 将数据保存到 order  orderDetail  orderStatus
        //订单过程
        //增加到三装裱
        //订单号的生成 锁定数据(stock库存) 分布式事务
        //订单创建成功后
        Order order = new Order() ;
        Long orderId= idWorker.nextId();
        order.setOrderId(orderId);//订单号如何生成 ， 没有采取自增测略
        order.setPaymentType(orderBo.getPayMentType());
        order.setPostFee(0L);//邮费
        order.setCreateTime(new Date());// 创建时间
        order.setUserId(info.getId());//用户信息
        order.setBuyerMessage(orderBo.getBuyerMessage());//留言
        order.setBuyerNick(info.getUsername());//用户的昵称
        order.setBuyerRate(false);//是否评价
        //订单的收获人信息 orderBo.getAddressId() 需要通过地址id查其他信息
        //查询地址信息 模拟的
        AddressBo address= AddressClient.getAddressByID(orderBo.getAddressId());
        order.setReceiver(address.getName());
        order.setReceiverMobile(address.getPhone());
        order.setReceiverState(address.getState());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverAddress(address.getAddress());
        order.setReceiverZip(address.getZipCode());
        //----------------------------------------
        //发票信息
        order.setInvoiceType(orderBo.getInvoiceType());
        //订单来源：1app 2pc 3M端 4微信 5 手机qq ....
        order.setSourceType(2);

        /*
        private String promotionIds; // 参与促销活动的id
        物流信息喝物流订单不用填写
        private String shippingName;// 物流名称
        private String shippingCode;// 物流单号

*/
       /* private Long totalPay;
        private Long actualPay;// 实付金额*/
        // 总金额
        Long totalPay=0L;
        //OrderDetail 保存
        List<OrderDetail> detailList= new ArrayList<>();
        for (CartBo cartBo: orderBo.getCartList()){
            //查询出sku的信息 --》调用item服务
            Sku sku= goodsClient.queryCartBySkuId(cartBo.getSkuId());
            OrderDetail detail= new OrderDetail();
            detail.setTitle(sku.getTitle());
            detail.setSkuId(sku.getId());
            detail.setPrice(sku.getPrice());
            detail.setOwnSpec(sku.getOwnSpec());
            //订单id
            detail.setOrderId(orderId);
            detail.setNum(cartBo.getNum());
            //取第一张图片
            detail.setImage(StringUtils.isNotEmpty(sku.getImages())?sku.getImages().split(",")[0]:"");
            detailList.add(detail);
            //总价格 计算
            totalPay+=(sku.getPrice().longValue()*cartBo.getNum().longValue());

        }
        //设置总金额
        order.setTotalPay(totalPay);
        //实付金额
        order.setActualPay(totalPay+order.getPostFee());
        //OrderStatus 订单状态
        OrderStatus orderStatus=new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCloseTime(new Date());
        orderStatus.setStatus(1);
        //测试 新增
        orderMapper.insert(order);
        orderDetailMapper.insertList(detailList);
        orderStatusMapper.insert(orderStatus);
        //将用户购买的数据 从库存中锁定
        //stock 放在 item中维护
        //需要通过item服务的方法进行删除
        detailList.forEach(orderDetail -> {
            Stock stock= new Stock();
            stock.setSkuId(orderDetail.getSkuId());
            stock.setStock(orderDetail.getNum());
          Boolean flag=  goodsClient.updateStock(stock);
            if (!flag){//库存失败 需要抛出异常
                throw new MrException(ExceptionEnums.SKU_STOCK_DE_ERROR);
            }
        });
       return orderId;
    }
}
