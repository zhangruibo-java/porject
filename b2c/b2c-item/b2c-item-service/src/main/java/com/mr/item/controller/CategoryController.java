package com.mr.item.controller;

import com.mr.common.advice.ExceptionEnums;
import com.mr.common.advice.MrException;
import com.mr.item.service.CategoryService;
import com.mr.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    /*
    *返回分类集合,根据pid
    *@return
    * */
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> list(@RequestParam("pid") Long pid){
       List<Category> list = categoryService.queryCategoryByPid(pid);
        System.out.println("5555555555555555555555");
        if(list.size() == 0){
        throw new MrException(ExceptionEnums.CATEGORY_LIST_NOT_FOUNT);
        }
        return ResponseEntity.ok(list);
    }
}
