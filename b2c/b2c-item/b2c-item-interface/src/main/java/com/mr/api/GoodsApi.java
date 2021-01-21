package com.mr.api;

import com.mr.bo.SpuBo;
import com.mr.common.utils.PageResult;
import com.mr.pojo.Sku;
import com.mr.pojo.Spu;
import com.mr.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("goods")
public interface GoodsApi {
    @GetMapping("list")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam("key") String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) String sale
    ) ;
    @PostMapping("addGoods")
    public Void addGoods(@RequestBody SpuBo spuBo);
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail  queryDetail(@PathVariable("spuId") Long spuId);

    @GetMapping("querySku/{spuId}")
    public List<Sku>  querySkuList(@PathVariable("spuId") Long spuId);

    @PutMapping("updateGood")
    public List<SpuBo> updateGoods(@RequestBody SpuBo spuBo);

    /**
     * 根据spuIdc查询spu
     * @param spuId
     * @return
     */
    @GetMapping("spu/{spuId}")
    public Spu querySpuBySpuId(@PathVariable("spuId") Long spuId);
}
