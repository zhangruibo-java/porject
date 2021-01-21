package com.mr.item.mapper;

import com.github.pagehelper.Page;
import com.mr.bo.SpuBo;
import com.mr.common.utils.PageResult;
import com.mr.pojo.Brand;
import com.mr.pojo.Spu;
import com.mr.pojo.SpuDetail;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Mapper
public interface GoodsMapper extends tk.mybatis.mapper.common.Mapper<Spu>{

  /* @Results({
            @Result(column = "id" ,property = "id"),
            @Result(column = "title" ,property = "title"),
            @Result(column = "sub_title" ,property = "subTitle"),
            @Result(column = "cid1" ,property = "cid1"),
            @Result(column = "cid2" ,property = "cid2"),
            @Result(column = "cid3" ,property = "cid3"),
            @Result(column = "brand_id" ,property = "brandId"),
            @Result(column = "saleable" ,property = "saleable"),
            @Result(column = "valid" ,property = "valid"),
            @Result(column = "title" ,property = "title"),
            @Result(column = "create_time" ,property = "createTime"),
            @Result(column = "last_update_time" ,property = "lastUpdateTime")
    })
    @Select("select * from tb_spu where 1=1 and title  like '%',#{title},'%' limit #{page},#{rows}")//
    List<Spu> querySpuByPage(String key, Integer page1, Integer rows, String sale);
//select * from tb_spu where 1 = 1 and  title like '%三%' limit 1,5 //
    @Select("select count(1) from tb_spu ")
    Long getCount();

    @Results({
            @Result(column = "id" ,property = "id"),
            @Result(column = "title" ,property = "title"),
            @Result(column = "sub_title" ,property = "subTitle"),
            @Result(column = "cid1" ,property = "cid1"),
            @Result(column = "cid2" ,property = "cid2"),
            @Result(column = "cid3" ,property = "cid3"),
            @Result(column = "brand_id" ,property = "brandId"),
            @Result(column = "saleable" ,property = "saleable"),
            @Result(column = "valid" ,property = "valid"),
            @Result(column = "title" ,property = "title"),
            @Result(column = "create_time" ,property = "createTime"),
            @Result(column = "last_update_time" ,property = "lastUpdateTime")
    })
    @Select("select * from tb_spu limit #{page1},#{rows}")
    List<Spu> querySpuPage(String key, Integer page1, Integer rows, String sale);

    //List<Spu> selectByRowBounds(String key, Integer page1, Integer rows);*/
  /*@Insert("INSERT into tb_spu_detail(spu_id,description,generic_spec,special_spec,packing_list,after_service)values(#{spuId},#{description},#{genericSpec},#{specialSpec},#{packingList},#{afterService})")
  void addGoods(SpuBo spuDetail);*/

}
