package com.mr.fegin;

import com.mr.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ITEM-SERVICE")
public interface GoodClient extends GoodsApi {

}

