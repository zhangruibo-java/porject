package com.mr.api;

import com.mr.pojo.SpecGroup;
import com.mr.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/spec")
public interface SpecApi {
    @GetMapping("/groups/{cid}")
    public List<SpecGroup> groups(@PathVariable("cid") Long cid);

    @PostMapping("/group")
    public Void addGroup(@RequestBody SpecGroup specGroup);

    @PutMapping("/group")
    public Void updateGroup(@RequestBody SpecGroup specGroup);

    @DeleteMapping("group/{id}")
    public  Void deleteGroup(@PathVariable("id") Long gid);

    /**
     *  根据规格组id查询规格参数
     * @param gid 根据组id查询规格
     * @param cid 根据分类id查询规格
     * @param searching 是否为搜索规格
     * @param generic  是否为通用规格 #此参数为新增
     * @return
     */
    @GetMapping("/params")
    public List<SpecParam> querySpecParam(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic
    );

    @PostMapping("/param")
    public Void addParam(@RequestBody SpecParam specParam);

    @PutMapping("/param")
    public Void updateParam(@RequestBody SpecParam specParam);
    @DeleteMapping("/param/{id}")
    public Void deleteParam(@PathVariable("id") Long id);


  @GetMapping("query")
  public List<SpecParam> querySpecByParamCid(@RequestParam("cid") Long cid);


}
