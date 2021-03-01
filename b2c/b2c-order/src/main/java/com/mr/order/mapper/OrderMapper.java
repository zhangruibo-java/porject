package com.mr.order.mapper;

import com.mr.order.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface OrderMapper extends tk.mybatis.mapper.common.Mapper<Order> {
    /**
     * 查询订单分页数据，
     * @param userId
     * @param status
     * @return
     */
    List<Order> queryOrderList(
            @Param("userId") Long userId,
            @Param("status") Integer status);
}
