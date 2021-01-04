package com.mr.item.controller;

import com.mr.item.service.SpecGroupService;
import com.mr.pojo.SpecGroup;
import com.mr.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> groups(@PathVariable("cid") Long cid){
        List<SpecGroup> list = specGroupService.querySpecBycid(cid);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/group")
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup specGroup){
        specGroupService.addGroup(specGroup);
        return ResponseEntity.status(201).body(null);
    }

    @PutMapping("/group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){
        specGroupService.updateGroup(specGroup);
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long gid){
        specGroupService.deleteGroup(gid);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> params(@RequestParam("gid") Long gid){
        List<SpecParam> list = specGroupService.querySpecBygid(gid);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam){
        specGroupService.addParam(specParam);
        return ResponseEntity.status(201).body(null);
    }

    @PutMapping("/param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam){
        specGroupService.updateParam(specParam);
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/param/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable("id") Long id){
        specGroupService.deleteParam(id);
        return ResponseEntity.ok(null);
    }

}
