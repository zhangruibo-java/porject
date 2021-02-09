package com.mr.fegin;

import com.mr.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("ITEM-SERVICE")
@RequestMapping("category")
public interface CategoryClient extends CategoryApi {

}

