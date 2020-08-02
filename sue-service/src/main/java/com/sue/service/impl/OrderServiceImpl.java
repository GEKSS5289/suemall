package com.sue.service.impl;

import com.sue.enums.YesOrNO;
import com.sue.mapper.OrdersMapper;
import com.sue.pojo.Orders;
import com.sue.pojo.UserAddress;
import com.sue.pojo.dto.SubmitOrderDTO;
import com.sue.service.AddressService;
import com.sue.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.Order;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sue
 * @date 2020/8/2 16:28
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrdersMapper ordersMapper;


    @Autowired
    private AddressService addressService;

    @Autowired
    private Sid sid;

    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createOrder(SubmitOrderDTO submitOrderDTO) {


         String userId  =  submitOrderDTO.getUserId();
         String addressId = submitOrderDTO.getAddressId();
         String itemSpecIds = submitOrderDTO.getItemSpecIds();

//         Integer payMethod = submitOrderDTO.getPayMethod();
//         String leftMsg = submitOrderDTO.getLeftMsg();




        //查询用户具体地址
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        //1.新订单保存
         String orderId = sid.nextShort();
         Orders newOrders = new Orders(orderId,submitOrderDTO,userAddress);


        //2.循环根据itemSpecIds保存订单商品信息表

        //3.保存订单状态表

    }
}
