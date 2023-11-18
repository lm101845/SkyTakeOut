package com.sky.task;

/**
 * @Author liming
 * @Date 2023/11/13 15:22
 * @Description
 **/

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的方法
     */
    @Scheduled(cron = "0 * * * * ? ")  //每分钟执行一次
    //@Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);//当前时间减去15分钟
        //select * from orders where status = ? and order_time < (当前时间 - 15分钟)
        final List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if(orderList != null && orderList.size() > 0){
             for (Orders orders : orderList) {
                 orders.setStatus(Orders.CANCELLED);
                 orders.setCancelReason("订单超时，系统自动取消");
                 orders.setCancelTime(LocalDateTime.now());
                 orderMapper.update(orders);
             }
        }
    }

    /**
     * 处理一直处于派送中状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")  //每天凌晨一点触发一次
    //@Scheduled(cron = "0/5 * * * * ?")
    public void processDeliverOrder(){
        log.info("定时处理一直处于派送中状态的订单:{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if(orderList != null && orderList.size() > 0){
            for (Orders orders : orderList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
