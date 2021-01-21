package com.mr.client;

import com.mr.api.CategoryApi;
import com.mr.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("ITEM-SERVICE")
@RequestMapping("category")
public interface CategoryClient extends CategoryApi {

}

