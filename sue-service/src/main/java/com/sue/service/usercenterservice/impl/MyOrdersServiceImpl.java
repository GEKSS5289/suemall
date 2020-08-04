package com.sue.service.usercenterservice.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sue.mapper.OrdersMapper;
import com.sue.pojo.vo.MyOrdersVO;
import com.sue.service.usercenterservice.MyOrdersService;
import com.sue.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sue
 * @date 2020/8/4 8:35
 */

@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }
        PageHelper.startPage(page,pageSize);
        List<MyOrdersVO> myOrdersVOS = ordersMapper.queryMyOrders(map);

        PageInfo<?> pageInfo = new PageInfo<>(myOrdersVOS);
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRows(myOrdersVOS);
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());


        return pagedGridResult;
    }
}
