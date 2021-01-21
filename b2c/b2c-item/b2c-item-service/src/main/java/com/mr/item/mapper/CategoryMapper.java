package com.mr.item.mapper;

import com.mr.pojo.Category;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends tk.mybatis.mapper.common.Mapper<Category>
    , SelectByIdListMapper<Category,Long>
{
//批量操作的api
    @Select("SELECT * FROM tb_category where parent_id = #{parentId}")
    @Results({
            @Result(column = "id" ,property = "id"),
            @Result(column = "name" ,property = "name"),
            @Result(column = "parent_id" ,property = "parentId"),
            @Result(column = "is_parent" ,property = "isParent"),
            @Result(column = "sort" ,property = "sort")
    })
    List<Category> queryCategoryByPid(Category category);

    int insert(Integer id);
    @Update("UPDATE tb_category set name = #{name} where id = #{id}")
    int updateCategory(Category category);

    @Select("select * FROM tb_spu  where  id in(cid1,cid2,cid3)")
    List<String> queryNameByIds(List<Long> asList);

    //@Insert("insert into tb_category(name,parent_id,is_parent,sort) values(#{name},#{parentId},#{isParent},#{sort})")


}
