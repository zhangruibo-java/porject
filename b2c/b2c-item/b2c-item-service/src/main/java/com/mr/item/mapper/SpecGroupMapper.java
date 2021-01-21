package com.mr.item.mapper;
import com.mr.pojo.SpecGroup;
import com.mr.pojo.SpecParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface SpecGroupMapper extends tk.mybatis.mapper.common.Mapper<SpecParam> {

  @Select("SELECT * FROM tb_spec_group WHERE cid=#{cid}")
    List<SpecGroup> querySpecBycid(Long cid);

    @Insert("Insert into tb_spec_group(cid,name) values(#{cid},#{name})")
    void addGroup(SpecGroup specGroup);

    @Insert("insert into tb_spec_param(cid,group_id) values(#{cid},#{gid})")
    void add(Long gid, Long cid);

    @Update("update tb_spec_group set name=#{name},cid=#{cid} where id=#{id}")
    void updateGroup(SpecGroup specGroup);

    @Delete("delete from tb_spec_group where id=#{gid}")
    void deleteGroup(Long gid);

//    @Select("select * from tb_spec_param where group_id=#{cid}")
//    List<SpecParam> querySpecBygid(Long cid);

    @Insert("insert into tb_spec_param values(null,#{cid},#{groupId},#{name},#{numeric},#{unit},#{generic},#{searching},#{segments})")
    void addParam(SpecParam specParam);

    @Update("update tb_spec_param set cid=#{cid},group_id=#{groupId}," +
            "name=#{name},numeric=#{numeric},unit=#{unit},generic=#{generic},searching=#{searching},segments=#{segments}")
    void updateParam(SpecParam specParam);

    @Delete("delete from tb_spec_param where id=#{id}")
    void deleteParam(Long id);


}
