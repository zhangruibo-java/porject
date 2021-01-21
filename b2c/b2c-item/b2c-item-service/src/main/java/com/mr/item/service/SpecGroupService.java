package com.mr.item.service;


import com.mr.item.mapper.SpecGroupMapper;

import com.mr.item.mapper.SpecParamMapper;
import com.mr.pojo.SpecGroup;
import com.mr.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;


    @Autowired
    private   SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecBycid(Long cid) {
        return specGroupMapper.querySpecBycid(cid);
    }

    public void addGroup(SpecGroup specGroup) {

        specGroupMapper.addGroup(specGroup);
    }

    public void updateGroup(SpecGroup specGroup) {
        specGroupMapper.updateGroup(specGroup);
    }

    public void deleteGroup(Long gid) {
        specGroupMapper.deleteGroup(gid);
    }

    public List<SpecParam> querySpecBygid(Long cid,Boolean searching) {
        //  return specGroupMapper.querySpecBygid(cid);
        SpecParam specParam = new SpecParam();
        specParam.setSearching(searching);
        specParam.setCid(cid);


        return specParamMapper.select(specParam);
    }

    public void addParam(SpecParam specParam) {
        specGroupMapper.addParam(specParam);
    }

    public void updateParam(SpecParam specParam) {
        specGroupMapper.updateParam(specParam);
    }

    public void deleteParam(Long id) {
        specGroupMapper.deleteParam(id);
    }

    public List<SpecParam> querySpecByParamCid(Long cid) {

        /* return  specParamMapper.querySpecByParamCid(cid);*/
        return (List<SpecParam>) specParamMapper.selectByPrimaryKey(cid);
    }


}
