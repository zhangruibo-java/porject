package com.mr.client;

import com.mr.api.BrandApi;
import com.mr.api.SpecApi;
import com.mr.pojo.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient("ITEM-SERVICE")
public interface SpecClient extends SpecApi {

}
