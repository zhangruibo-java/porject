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

    @Autowired
    private GoodsService goodsService;
    /*
     * 根据pid查询数据
     * @param
     * @return*/


    public List<Category> queryCategoryByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.queryCategoryByPid(category);
    }


    public int insert(Category id) {

        System.out.println(id);
        return categoryMapper.insert(id);
    }

    public void deleteCategory(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    public int updateCategory(Category category) {

        return categoryMapper.updateCategory(category);
    }


    public List<String> queryNameByIds(List<Long> asList) {
        return categoryMapper.queryNameByIds(asList);
    }

    public List<Category> queryCateboryList(List<Long> cids) {
        return categoryMapper.selectByIdList(cids);
    }
}
