package com.mr.controller;

import com.mr.service.GoodPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller//用Controller 可以返回页面
@RequestMapping("item")
public class GoodPageController {
    @Autowired
    private GoodPageService goodPageService;
    /**
     * 跳转商品详情
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String toGoodInfo(@PathVariable("id") Long id, ModelMap map){
        System.out.println("商品详情："+id);

        Map<String,Object> modelMap= goodPageService.getGoodInfo(id);
        //同步\请求 modelAndView or modelMap
        map.putAll(modelMap);
        return "item";

    }
}
