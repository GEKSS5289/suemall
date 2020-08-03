package com.sue.service.mallservice.impl;

import com.sue.enums.OrderStatusEnum;
import com.sue.mapper.OrderItemsMapper;
import com.sue.mapper.OrderStatusMapper;
import com.sue.mapper.OrdersMapper;
import com.sue.pojo.*;
import com.sue.pojo.dto.malldto.SubmitOrderDTO;
import com.sue.pojo.vo.MerchantOrdersVO;
import com.sue.pojo.vo.OrderVO;
import com.sue.service.mallservice.AddressService;
import com.sue.service.mallservice.ItemService;
import com.sue.service.mallservice.OrderService;
import com.sue.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/2 16:28
 */

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;


    @Autowired
    private Sid sid;

    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderDTO submitOrderDTO) {


        String userId = submitOrderDTO.getUserId();
        String addressId = submitOrderDTO.getAddressId();
        String itemSpecIds = submitOrderDTO.getItemSpecIds();

//         Integer payMethod = submitOrderDTO.getPayMethod();
//         String leftMsg = submitOrderDTO.getLeftMsg();


        //查询用户具体地址
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        //1.新订单保存
        String orderId = sid.nextShort();
        Orders newOrders = new Orders(orderId, submitOrderDTO, userAddress);


        //2.循环根据itemSpecIds保存订单商品信息表
        String itemSpecIdArr[] = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0; //优惠后的价格累计

        for (String itemSpecId : itemSpecIdArr) {

            //TODO 整合redis之后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;


            //2.1 根据规格Id，查询规格的具体信息，主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;


            //2.2 根据规格id，获得商品信息及商品图片
            String itemId = itemsSpec.getItemId();
            //获取商品信息
            Items items = itemService.queryItemById(itemId);
            //获取商品图片信息
            String imgUrl = itemService.queryItemMainImgById(itemId);


            OrderItems subOrderItem = new OrderItems();
            String subOrderId = sid.nextShort();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(items.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());


            orderItemsMapper.insert(subOrderItem);

            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);

        }


        newOrders.setTotalAmount(totalAmount);
        newOrders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrders);


        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());

        orderStatusMapper.insert(waitPayOrderStatus);


        //构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + 0);
        merchantOrdersVO.setPayMethod(submitOrderDTO.getPayMethod());

        //构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        return orderVO;
    }


    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {

        OrderStatus padiStatus = new OrderStatus();

        padiStatus.setOrderId(orderId);
        padiStatus.setOrderStatus(orderStatus);
        padiStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(padiStatus);

    }

    /**
     * 查询订单状态
     *
     * @param orderId
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 关闭超时未支付订单
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        //查询所有为付款订单，判断时间是否超时（1天）,超时则关闭交易
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> notpayLists = orderStatusMapper.select(queryOrder);
        notpayLists.forEach(i->{
            //获得订单创建时间
            Date createdTime = i.getCreatedTime();
            int days = DateUtil.daysBetween(createdTime, new Date());
            if(days >=1){
                //超过1天，关闭订单
                doCloseOrder(i.getOrderId());
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId){
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);
    }

}
