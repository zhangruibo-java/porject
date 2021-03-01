package com.mr.cart.fiegn;

import com.mr.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodClient extends GoodsApi {
}
