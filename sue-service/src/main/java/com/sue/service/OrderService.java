package com.sue.service;

import com.sue.pojo.OrderStatus;
import com.sue.pojo.dto.SubmitOrderDTO;
import com.sue.pojo.vo.OrderVO;

/**
 * @author sue
 * @date 2020/8/2 16:28
 */

public interface OrderService {
    /**
     * 用于创建订单相关信息
     * @param submitOrderDTO
     */
    public OrderVO createOrder(SubmitOrderDTO submitOrderDTO);


    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId,Integer orderStatus);


    /**
     * 查询订单状态
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

}
