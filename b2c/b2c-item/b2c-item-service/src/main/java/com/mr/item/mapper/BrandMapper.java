package com.mr.item.mapper;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Brand;
import com.mr.pojo.Category;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Mapper
public interface BrandMapper extends tk.mybatis.mapper.common.Mapper<Brand>{
    @Select("select * from tb_brand")
    @Results({
            @Result(column = "id" ,property = "id"),
            @Result(column = "name" ,property = "name"),
            @Result(column = "image" ,property = "image"),
            @Result(column = "letter" ,property = "letter")})

    public PageResult<Brand> queryBrandPage();

    Object queryBrandPage(Example op);

    @Insert("insert into tb_category_brand(brand_id,category_id) values(#{bid},#{cid})")
    void insertBrand(Long bid, Long cid);

    @Select("SELECT NAME,ID FROM tb_category WHERE ID IN(SELECT category_id FROM tb_category_brand c WHERE c.brand_id=#{bid})")
    public  List<Category> queryCateBrand(Long bid);

/*
    @Insert("insert into tb_brand(name,letter) values(#{name},#{letter})")
    void addBrand(Brand brand);*/
}
