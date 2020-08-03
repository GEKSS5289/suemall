package com.sue.config;

import com.sue.pojo.OrderStatus;
import com.sue.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author sue
 * @date 2020/8/3 12:21
 */

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoColose(){
        orderService.closeOrder();
        System.out.println("执行任务");
    }
}

