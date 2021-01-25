package com.mr.bo;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Brand;
import com.mr.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult<T> extends PageResult{
//拓展搜索结果
    //分类结果
    private List<Category> categoryList;
    //品派结果
    private List<Brand> brandList;
    //规格结果
    private List<Map<String,Object>> specMapList;
}
