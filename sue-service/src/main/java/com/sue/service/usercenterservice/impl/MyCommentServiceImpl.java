package com.sue.service.usercenterservice.impl;

import com.github.pagehelper.PageHelper;
import com.sue.enums.YesOrNO;
import com.sue.mapper.ItemsCommentsMapper;
import com.sue.mapper.OrderItemsMapper;
import com.sue.mapper.OrderStatusMapper;
import com.sue.mapper.OrdersMapper;
import com.sue.pojo.OrderItems;
import com.sue.pojo.OrderStatus;
import com.sue.pojo.Orders;
import com.sue.pojo.dto.usercenterdto.OrderItemsCommentDTO;
import com.sue.pojo.vo.MyCommentVO;
import com.sue.service.usercenterservice.MyCommentService;
import com.sue.utils.PagedGridResult;
import com.sue.utils.PagedGridResultUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sue
 * @date 2020/8/4 10:11
 */
@Service
public class MyCommentServiceImpl implements MyCommentService {

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    /**
     * 根据订单id查询关联商品
     *
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }


    /**
     * 保存用户的评价
     *
     * @param orderId
     * @param userId
     * @param commentDTOS
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentDTO> commentDTOS) {

        //1.保存评价 items_comments

        for (OrderItemsCommentDTO oic : commentDTOS) {
            oic.setCommentId(sid.nextShort());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", commentDTOS);
        itemsCommentsMapper.saveComments(map);


        //2.修改订单表已评价
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNO.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);


        //3.修改订单状态表的留言时间
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);


    }


    /**
     * 我的评价查询分页
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> myCommentVOS = itemsCommentsMapper.queryMyComments(map);

        return PagedGridResultUtils.setterPagedGrid(myCommentVOS, page);
    }
}
