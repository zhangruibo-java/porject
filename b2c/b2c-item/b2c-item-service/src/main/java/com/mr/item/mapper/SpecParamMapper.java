package com.mr.item.mapper;

import com.mr.pojo.SpecParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecParamMapper extends tk.mybatis.mapper.common.Mapper<SpecParam>{
    /*@Select("select * from tb_spec_param where cid=#{cid}")
    List<SpecParam> querySpecByParamCid(Long cid);*/
}
