package com.mr.order.controller;

import com.mr.bo.UserInfo;
import com.mr.order.bo.OrderBo;
import com.mr.order.config.JwtConfig;
import com.mr.order.service.OrderService;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制层
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtConfig jwtConfig;
    /**
     * 创建订单对象
     * @param orderBo
     * @param token
     * @return
     */
    @PostMapping("createOrder")
    public ResponseEntity<Long> createOrder(
            @RequestBody OrderBo orderBo,
            @CookieValue("B2C_TOKEN") String token){
        System.out.println(orderBo.getAddressId());
        Long orderId = null;
        try{
            //解析用户登录对象
        UserInfo info = JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
        //订单创建
       orderId=  orderService.createOrder(orderBo,info);
            System.out.println("创建成功"+orderId);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(orderId);

    }}
