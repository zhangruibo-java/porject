package com.mr.item.mapper;

import com.mr.pojo.Spu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpuMapper extends tk.mybatis.mapper.common.Mapper<Spu>{
    @Insert("INSERT INTO tb_spu(title,sub_title,cid1,cid2,cid3,brand_id,saleable,valid,create_time,last_update_time)" +
            " VALUES(#{title},#{subTitle},#{cid1},#{cid2},#{cid3},#{brandId},#{saleable},#{valid},#{createTime},#{lastUpdateTime})")
    void add(Spu spu);

    /*
    *   private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架
    private Boolean valid;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间
    * */
}
