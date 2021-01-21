package com.mr.controller;

import com.mr.bo.SearchBo;
import com.mr.common.utils.PageResult;
import com.mr.pojo.Goods;
import com.mr.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("good")
public class GoodController {

    @Autowired
    private GoodService goodService;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> searchGood(@RequestBody SearchBo searchBo){
        PageResult<Goods>  pageResult = goodService.searchGood(searchBo);
        return ResponseEntity.ok(pageResult);
    }
}
