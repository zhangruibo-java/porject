package com.mr.cart.controller;

import com.mr.bo.UserInfo;
import com.mr.cart.bo.Cart;
import com.mr.cart.config.JwtConfig;
import com.mr.cart.service.CartService;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("cart")
public class CartController {
    //购物车需要用户登录才能操作
    //能够通过网关路由到购物车服务，就已经登录了
    //可以加过滤器 也可以不加
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private CartService cartService;
    /**
     * 购物车的增加
     * @param cart
     * @param token 用户的token
     * @return  http://api.b2c.com/api/cart/addCart
     */
    @PostMapping("addCart")
    public ResponseEntity<Cart> addCart(@RequestBody Cart cart ,@CookieValue("B2C_TOKEN") String token){

       // 获得公钥

        // 解析token 获得y用户id
        try{
      UserInfo userInfo= JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
      //增加购物车到缓存
           cartService.addCart(cart,userInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
      return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车数据
     * @param token
     * @return
     * @return
     */
    @GetMapping("queryCartList")
    public ResponseEntity<List<Cart>> queryCartList(@CookieValue("B2C_TOKEN") String token){
        try {
            //获得登陆用户数据
            UserInfo userInfo= JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
            //根据用户查询购物车数据
            List<Cart> cartList=cartService.queryCartList(userInfo);
            //如果无购物车数据则返回无数据状态
            if(cartList==null){
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.ok(cartList);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
    @PutMapping("updateNum")
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart,@CookieValue("B2C_TOKEN") String token){
        //解析用户id
        try{
          UserInfo userInfo=  JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
            cartService.updateNum(userInfo,cart);
        }catch(Exception e){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.ok(null);
    }
    @DeleteMapping("deleteCart/{skuId}")
    public ResponseEntity<Void> updateNum(@PathVariable("skuId") String skuId,@CookieValue("B2C_TOKEN") String token){
        //解析用户id
        try{
            UserInfo userInfo=  JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
            cartService.deleteCart(skuId,userInfo);
        }catch(Exception e){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.ok(null);
    }
}
