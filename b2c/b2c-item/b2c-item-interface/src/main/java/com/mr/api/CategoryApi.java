package com.mr.api;

import com.mr.common.advice.ExceptionEnums;
import com.mr.common.advice.MrException;
import com.mr.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("category")
public interface CategoryApi {

    @GetMapping("list")
    public List<Category> list(@RequestParam("pid") Long pid);
    @PostMapping("insert")
    public String addUser( @RequestBody Category Category);
    //http://api.b2c.com/api/item/category/deleteCategory1429
    @DeleteMapping("deleteCategory/{id}")
    public void deleteCategory(@PathVariable("id") Long id);
    @PutMapping("updateCategory")
    public String updateCategory(@RequestBody Category category);
    @GetMapping("queryCategoryList")
    public  List<Category> queryCategoryList(
            @RequestParam List<Long>  cids);

}
