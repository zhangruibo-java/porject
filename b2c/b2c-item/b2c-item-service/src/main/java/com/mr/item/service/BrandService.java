package com.mr.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.common.utils.PageResult;
import com.mr.item.mapper.BrandMapper;
import com.mr.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandPage(String key, Integer page, Integer rows) {
        //开始分页
        PageHelper.startPage(page,rows);
        Example op=new Example(Brand.class);

        if (StringUtils.isNotEmpty(key)){
            //非空验证
            op.createCriteria().andLike("name","%"+key+"%");
        }
        Page<Brand> list=(Page<Brand>) brandMapper.selectByExample(op);
        //返回分页数据
        return new PageResult<Brand>(list.getTotal(),list.getResult());
    }
}
