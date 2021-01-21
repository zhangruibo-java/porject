package com.mr.item.mapper;

import com.mr.bo.SpuBo;
import com.mr.pojo.SpuDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpuDetailMapper extends tk.mybatis.mapper.common.Mapper<SpuDetail>{
   @Insert("INSERT INTO tb_spu_detail(spu_id,description,special_spec,generic_spec,packing_list,after_service)" +
           " VALUES(#{spuId},#{description},#{specialSpec},#{genericSpec},#{packingList},#{afterService})")
   List<SpuBo> add(SpuDetail spuDetail);
   /*
   *private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specialSpec;// 商品特殊规格的名称及可选值模板
    private String genericSpec;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务
   *
   * */
}
