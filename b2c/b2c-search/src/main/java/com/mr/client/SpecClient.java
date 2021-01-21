package com.mr.client;

import com.mr.api.BrandApi;
import com.mr.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ITEM-SERVICE")
public interface SpecClient extends SpecApi {
}
