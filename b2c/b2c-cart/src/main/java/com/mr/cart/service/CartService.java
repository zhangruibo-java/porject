package com.mr.cart.service;

import com.mr.bo.UserInfo;
import com.mr.cart.bo.Cart;
import com.mr.cart.fiegn.GoodClient;
import com.mr.common.utils.JsonUtils;
import com.mr.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //key 的前缀
    private  final String KEY_PREFIX="b2c:cart:uid:";

    private static final String  SKUKEY_PREFIX="b2c:sku:id ";
    @Autowired
    private GoodClient goodClient;
    /**
     * 增加购物车方法
     * @param cart
     * @param user
     */
    public void addCart(Cart cart , UserInfo user){
//1 将购物场合数据登入到redis

        //1 cart中 sku数据需要填充
            //通过feign请求item调用sku查询方法
        //2 使用redis 用什么数据类型（5+3）
            //hash {key,{key,value}}
                //    {userId,{skuId,sku}}
            //hash的key不可以重复 购买的商品也不可重复
            //如果重复需要合并两次购买到数量

        //{b2c:cart:uid,{1,华为}} 绑定，只能操作一个key
        BoundHashOperations<String,Object,Object> hashOps= stringRedisTemplate.boundHashOps(KEY_PREFIX+user.getId());
        //put到购物车 加入到redis
        //判断此次商品有没有被购买过
        //hashOps.put(cart.getSkuId(),cart);
        //本次购买的商品id及数量
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean flag = hashOps.hasKey(skuId.toString());
        if(flag){//存在购买过 两次购买的数量合并
            //本次购买数量加购物车原有购买数量
          String json=  hashOps.get(skuId.toString()).toString();
            //将json反序列化 转为对象
            cart = JsonUtils.parse(json,Cart.class);
            //购买合并
            cart.setNum(num+cart.getNum());


        }else{//没有购买过 直接填充数据到缓存
            cart.setUserId(user.getId());
            //填充其他数据，前端只传递了id和数量    调用feign 查询item
          Sku sku= goodClient.queryCartBySkuId(skuId);
          //判断图片是不是一张 如果是就选择下标0 如果不是就穿个空
            /*cart.setImage(StringUtils.isNotEmpty(sku.getImages())?sku.getImages().split(",")[0]:"");*/
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());

        }
        //redis保存对象需要进行序列化（jdk json）
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    public List<Cart> queryCartList(UserInfo userInfo) {
        // 判断用户是否有购物车数据
        String key = KEY_PREFIX + userInfo.getId();
        if(!stringRedisTemplate.hasKey(key)){
            //不存在 直接返回
            return null;
        }
        //获得绑定key的hash对象
        BoundHashOperations<String,Object,Object> hashOps= stringRedisTemplate.boundHashOps(key);
        //获得数据 需要从json转换为对象
        List<Object> cartJsonList=hashOps.values();
        if(CollectionUtils.isEmpty(cartJsonList)){
            return null;
        }
        List<Cart> cartList=cartJsonList.stream().map(obj->{
            return JsonUtils.parse(obj.toString(),Cart.class);
        }).collect(Collectors.toList());
        //数据库价格发生改变 购物车商品价格也要发生改变
        cartList.forEach(cart->{
            //cart 是购物车内数据
            //查询商品最新数据
          //  Sku sku=goodClient.queryCartBySkuId(cart.getSkuId());
            //查询商品最新数据(从缓存中查询，避免mysql频繁操作)
            Sku sku=this.querySku(cart.getSkuId());
            //对比价格 当价格发生变化
            if(sku.getPrice().intValue()!=cart.getPrice().intValue()){
            Long  oldPrice=cart.getPrice();
            cart.setOldPrice(oldPrice);
            cart.setPrice(sku.getPrice());
            }
            //库存是否大于要购买量
            cart.setStock(sku.getStock());
            //商品是否下架
            cart.setEnable(sku.getEnable());
        });
        return cartList;
    }

    /**
     * 从缓存中查询数据
     * @param skuId
     * @return
     */
    public Sku querySku(Long skuId){
        Sku sku=null;
        //缓存中没有sku数据
        String key=SKUKEY_PREFIX+skuId;
        BoundValueOperations<String,String> stringOps=  stringRedisTemplate.boundValueOps(key);
        //获得sku数据
        String skuJson=stringOps.get();
        //如果没有sku 数据
        if(StringUtils.isEmpty(skuJson)){
            //手动添加
             sku=goodClient.queryCartBySkuId(skuId);
            stringOps.set(JsonUtils.serialize(sku));
            //设置过期时间 是为了 防止热度不高的sku占用redis缓存

            stringRedisTemplate.expire(key,30, TimeUnit.DAYS);
        return sku;
        }else{
            //缓存中存在sku数据 直接反序列化返回
            sku=JsonUtils.parse(skuJson.toString(),Sku.class);
        }

        return sku;
    }

    /**
     * 修改购买数量
     * @param userInfo
     * @param cart
     */

    public void updateNum(UserInfo userInfo, Cart cart) {
    //存在redis hash 内存当中
       //组装key
        String key= KEY_PREFIX+userInfo.getId();
       //获得hash对象
        BoundHashOperations<String,Object,Object> hashOps=  stringRedisTemplate.boundHashOps(key);
    //修改某个商品的购买数量
        //查询混村内的cart对象
        String cartJson= hashOps.get(cart.getSkuId().toString()).toString();
        Cart cacheCart=JsonUtils.parse(cartJson,Cart.class);
        //设置修改后的数量 不关心原有的数量 进行覆盖
        cacheCart.setNum(cart.getNum());
        //设置到缓存当中
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cacheCart));
    }

    /**
     * s]删除缓存内数据
     * @param skuId
     * @param userInfo
     */
    public void deleteCart(String skuId, UserInfo userInfo) {
        String key= KEY_PREFIX+userInfo.getId();
        //获得hash对象
        BoundHashOperations<String,Object,Object> hashOps=  stringRedisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
