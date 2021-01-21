package com.mr.item.mapper;

import com.mr.bo.SpuBo;
import com.mr.pojo.Sku;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuMapper extends tk.mybatis.mapper.common.Mapper<Sku>{
    @Insert("INSERT INTO tb_sku(spu_id,title,images,price,own_spec,indexes,create_time,last_update_time) VALUES(#{spuId},#{title},#{images},#{price},#{ownSpec},#{indexes},#{createTime},#{lastUpdateTime})")
   List<SpuBo> add(Sku sku);
/*
private Long id;
private Long spuId;
private String title;
private String images;
private Long price;
private String ownSpec;// 商品特殊规格的键值对
private String indexes;// 商品特殊规格的下标
private Boolean enable;// 是否有效，逻辑删除用
private Date createTime;// 创建时间
private Date lastUpdateTime;// 最后修改时间
* */
}
