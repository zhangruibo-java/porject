package com.mr.api;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Brand;
import com.mr.pojo.Category;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("page")//http://api.b2c.com/api/item/brand/page
    public PageResult<Brand> queryBrandPage(
            @RequestParam("key") String key,
            @RequestParam("page")    Integer page,
            @RequestParam("rows")    Integer rows
    );
    //http://api.b2c.com/api/item/brand/addBrand
    @PostMapping("addBrand")
    public void addBrand(@RequestParam Brand brand,  @RequestParam String cids);
    //根据品牌 查询分类
    @GetMapping("queryCateBrand/{bid}")
    public List<Category> queryCateBrand(@PathVariable("bid") Long bid);
    @PutMapping("updateBrand")
    public void updateBrand(@RequestParam Brand brand, @RequestParam String cids);
    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandByCategory(@PathVariable("cid") Long cid);
    @GetMapping("bid/{bid}")
    public Brand queryBidById(@PathVariable("bid") Long bid);
}
