package com.mr.item.controller;

import com.mr.common.utils.PageResult;
import com.mr.item.service.BrandService;
import com.mr.pojo.Brand;
import com.mr.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")//http://api.b2c.com/api/item/brand/page
    public ResponseEntity<PageResult<Brand>> queryBrandPage(
        @RequestParam("key") String key,
        @RequestParam("page")    Integer page,
        @RequestParam("rows")    Integer rows
    ){

        PageResult<Brand> pageResult = (PageResult<Brand>) brandService.queryBrandPage(key,page,rows);
        return ResponseEntity.ok(pageResult);
    }
    //http://api.b2c.com/api/item/brand/addBrand
    @PostMapping("addBrand")
    public ResponseEntity<Void> addBrand(@RequestParam Brand brand,@RequestParam String cids){
        brandService.addBrand(brand,cids);
        return ResponseEntity.status(201).body(null);
    }
    //根据品牌 查询分类
    @GetMapping("queryCateBrand/{bid}")
    public ResponseEntity<List<Category>> queryCateBrand(@PathVariable("bid") Long bid){
      List<Category> list=  brandService.queryCateBrand(bid);
        return ResponseEntity.ok(list);
    }
    @PutMapping("updateBrand")
    public ResponseEntity<Void> updateBrand(@RequestParam Brand brand,@RequestParam String cids){
        brandService.updateBrand(brand,cids);
        return  ResponseEntity.ok(null);
    }
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid") Long cid){
        List<Brand> list = brandService.queryBrandByCategory(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据id查询品牌
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<Brand> queryBidById(@PathVariable("bid") Long bid){
        Brand brand = brandService.queryBidById(bid);
        return ResponseEntity.ok(brand);
    }

}
