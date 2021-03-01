package com.mr.item.mapper;

import com.mr.pojo.Stock;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StockMapper extends tk.mybatis.mapper.common.Mapper<Stock>{

    /*
    *     @Id
    private Long skuId;
    private Integer seckillStock;// 秒杀可用库存
    private Integer seckillTotal;// 已秒杀数量
    private Integer stock;// 正常库存
    * */
    @Insert("INSERT INTO tb_stock(sku_id,stock) VALUES(#{skuId},#{stock})")
    void add(Stock stock);

    /**
     * 修改库存方法 防止超卖
     * @param stock
     * @return
     */
    @Update("update  tb_stock SET stock=stock-${stock} where sku_id=#{skuId} and stock>=#{stock}")
    public int  updateStock(Stock stock);
}
