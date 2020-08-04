package com.sue.mapper;

import com.sue.my.MyMapper;
import com.sue.pojo.Orders;
import com.sue.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapper extends MyMapper<Orders> {
    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String,Object> map);
}