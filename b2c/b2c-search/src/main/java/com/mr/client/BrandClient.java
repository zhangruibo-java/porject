package com.mr.client;

import com.mr.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("ITEM-SERVICE")
public interface BrandClient extends BrandApi {
}
