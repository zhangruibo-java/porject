package com.mr.item.service;


import com.mr.item.mapper.SpecParamMapper;

import com.mr.pojo.SpecGroup;
import com.mr.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecBycid(Long cid) {
        return specParamMapper.querySpecBycid(cid);
    }

    public void addGroup(SpecGroup specGroup) {

        specParamMapper.addGroup(specGroup);
    }

    public void updateGroup(SpecGroup specGroup) {
        specParamMapper.updateGroup(specGroup);
    }

    public void deleteGroup(Long gid) {
        specParamMapper.deleteGroup(gid);
    }

    public List<SpecParam> querySpecBygid(Long gid) {
        return specParamMapper.querySpecBygid(gid);
    }

    public void addParam(SpecParam specParam) {
        specParamMapper.addParam(specParam);
    }

    public void updateParam(SpecParam specParam) {
        specParamMapper.updateParam(specParam);
    }

    public void deleteParam(Long id) {
        specParamMapper.deleteParam(id);
    }
}
