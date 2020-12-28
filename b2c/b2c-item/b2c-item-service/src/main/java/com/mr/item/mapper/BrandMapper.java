package com.mr.item.mapper;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Mapper
public interface BrandMapper extends tk.mybatis.mapper.common.Mapper<Brand>{
    @Select("select * from tb_brand")
    public PageResult<Brand> queryBrandPage();

    Object queryBrandPage(Example op);
}
