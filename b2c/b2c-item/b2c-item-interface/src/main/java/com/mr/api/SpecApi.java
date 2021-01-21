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
     * 查询规格参数
     * @param cid
     * @return
     */
    @GetMapping("/params")
    public List<SpecParam> params(
            @RequestParam("cid") Long cid,
            @RequestParam("searching") boolean searching);

    @PostMapping("/param")
    public Void addParam(@RequestBody SpecParam specParam);

    @PutMapping("/param")
    public Void updateParam(@RequestBody SpecParam specParam);
    @DeleteMapping("/param/{id}")
    public Void deleteParam(@PathVariable("id") Long id);

//    @GetMapping("/params")
//    public ResponseEntity<List<SpecParam>> querySpecByParamCid(@RequestParam("cid") Long cid){
//        List<SpecParam> list = specGroupService.querySpecByParamCid(cid);
//        return ResponseEntity.ok(list);
//    }

    @GetMapping("query")
    public List<SpecParam> querySpecByParamCid(@RequestParam("cid") Long cid);
}
