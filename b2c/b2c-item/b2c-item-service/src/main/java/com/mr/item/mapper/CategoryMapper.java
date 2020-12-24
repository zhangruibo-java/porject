package com.mr.item.mapper;

import com.mr.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends tk.mybatis.mapper.common.Mapper<Category>{

    @Select("SELECT * FROM tb_category where parent_id = #{parentId}")
    @Results({
            @Result(column = "id" ,property = "id"),
            @Result(column = "name" ,property = "name"),
            @Result(column = "parent_id" ,property = "parentId"),
            @Result(column = "is_parent" ,property = "isParent"),
            @Result(column = "sort" ,property = "sort")
    })
    public List<Category> queryCategoryByPid(Category category);


}
