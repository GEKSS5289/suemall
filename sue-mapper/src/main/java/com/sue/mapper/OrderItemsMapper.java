package com.sue.mapper;

import com.sue.pojo.OrderItems;

public interface OrderItemsMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderItems record);

    int insertSelective(OrderItems record);

    OrderItems selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderItems record);

    int updateByPrimaryKey(OrderItems record);
}