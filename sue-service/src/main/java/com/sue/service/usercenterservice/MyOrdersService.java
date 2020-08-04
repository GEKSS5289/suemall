package com.sue.service.usercenterservice;

import com.sue.utils.PagedGridResult;

/**
 * @author sue
 * @date 2020/8/4 8:35
 */

public interface MyOrdersService {

    /**
     * 查询我的订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId,Integer orderStatus,
                                         Integer page,Integer pageSize);

}
