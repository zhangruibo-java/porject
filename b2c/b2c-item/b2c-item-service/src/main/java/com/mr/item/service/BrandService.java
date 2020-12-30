package com.mr.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.common.utils.PageResult;
import com.mr.item.mapper.BrandMapper;
import com.mr.pojo.Brand;
import com.mr.pojo.Category;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
    @Transactional
    public void addBrand(Brand brand,  String cids) {
        //品牌表
        brandMapper.insert(brand);
        //品牌 分类 关系表
        String[] arrCid = cids.split(",");
        for (String i : arrCid){
            brandMapper.insertBrand(brand.getId(),Long.valueOf(i));
        }

    }

    public List<Category> queryCateBrand(Long bid) {
      return    brandMapper.queryCateBrand(bid);
    }
    @Transactional
    public void updateBrand(Brand brand, String bids) {
        //修改品牌表
        brandMapper.updateBrand(brand);
        //  删除所有的关系
        brandMapper.deleteBrand(brand.getId());
        //重新增加所有的关系
        String[] arrCid = bids.split(",");
        for (String i : arrCid){
            brandMapper.insertBrand(brand.getId(),Long.valueOf(i));
        }
    }
}
