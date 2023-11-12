package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

/**
 * @Author liming
 * @Date 2023/11/12 13:08
 * @Description
 **/
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明情数据
     * @param orderDetailList
     */
    void insertBatch(ArrayList<OrderDetail> orderDetailList);
}
