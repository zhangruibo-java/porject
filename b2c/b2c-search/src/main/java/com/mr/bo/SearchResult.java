package com.mr.bo;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Brand;
import com.mr.pojo.Category;

import java.util.List;

public class SearchResult extends PageResult {
//拓展搜索结果
    //分类结果
    private List<Category> categoryList;
    //品派结果
    private List<Brand> brandList;
    //规格结果
}
