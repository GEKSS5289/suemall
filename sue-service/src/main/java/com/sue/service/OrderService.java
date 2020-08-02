package com.sue.service;

import com.sue.pojo.dto.SubmitOrderDTO;

/**
 * @author sue
 * @date 2020/8/2 16:28
 */

public interface OrderService {
    /**
     * 用于创建订单相关信息
     * @param submitOrderDTO
     */
    public void createOrder(SubmitOrderDTO submitOrderDTO);
}
