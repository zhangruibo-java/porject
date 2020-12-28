package com.mr.item.controller;

import com.mr.common.advice.ExceptionEnums;
import com.mr.common.advice.MrException;
import com.mr.item.service.CategoryService;
import com.mr.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("insert")
    public ResponseEntity<String> addUser( @RequestBody Category Category){
        categoryService.insert(Category);
        return ResponseEntity.ok("新增成功");
    }
    //http://api.b2c.com/api/item/category/deleteCategory1429
    @DeleteMapping("deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(null);
    }
    @PutMapping("updateCategory")
    public ResponseEntity<String> updateCategory(@RequestBody Category category){
       categoryService.updateCategory(category);
        System.out.println(category);
        return ResponseEntity.ok(null);
    }
}
