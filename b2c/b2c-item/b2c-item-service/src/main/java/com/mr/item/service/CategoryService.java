package com.mr.item.service;

import com.mr.item.mapper.CategoryMapper;
import com.mr.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
   /*
     * 根据pid查询数据
     * @param
     * @return*/


    public List<Category> queryCategoryByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.queryCategoryByPid(category);
    }
}
