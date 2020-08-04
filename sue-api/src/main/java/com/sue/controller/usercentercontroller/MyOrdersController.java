package com.sue.controller.usercentercontroller;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.exception.commonException.DataNullException;
import com.sue.exception.usercenterexception.MyOrderException;
import com.sue.pojo.Orders;
import com.sue.pojo.vo.OrderStatusCountsVO;
import com.sue.service.usercenterservice.MyOrdersService;
import com.sue.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sue
 * @date 2020/8/3 13:44
 */

@Api(value = "用户中心我的订单", tags = {"用户中心我的订单"})
@RestController
@RequestMapping("/myorders")
@Validated
public class MyOrdersController extends BaseController {


    @Autowired
    private MyOrdersService myOrdersService;


    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = true)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = true)
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @ApiParam(name = "pageSize", value = "每页显示条数", required = true)
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {

        if (StringUtils.isBlank(userId)) {
            throw new DataNullException(44400);
        }

        PagedGridResult pagedGridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }


    @ApiOperation(value = "用户确认收货", notes = "用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单Id", required = true)
            @RequestParam String orderId

    ) {
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (imoocjsonResult.getStatus() != HttpStatus.OK.value()) {
            return imoocjsonResult;
        }

        boolean b = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!b) {
            throw new MyOrderException(11001);
        }

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户删除订单", notes = "用户删除订单", httpMethod = "POST")
    @PostMapping("delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId
    ) {
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (imoocjsonResult.getStatus() != HttpStatus.OK.value()) {
            return imoocjsonResult;
        }
        boolean b = myOrdersService.deleteOrder(userId, orderId);
        if (!b) {
            throw new MyOrderException(11000);
        }
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "获得订单状态数概况",notes = "获得订单状态数概况",httpMethod = "POST")
    @PostMapping("/statusCounts")
    public IMOOCJSONResult statusCounts(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId
    ){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        OrderStatusCountsVO orderStatusCounts = myOrdersService.getOrderStatusCounts(userId);
        return IMOOCJSONResult.ok(orderStatusCounts);
    }



    @ApiOperation(value = "查询订单动向",notes="查询订单动向",httpMethod = "POST")
    @PostMapping("/trend")
    public IMOOCJSONResult trend(
            @ApiParam(name = "userId",value = "用户Id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "分页的每一页显示的条数",required = false)
            @RequestParam Integer pageSize
    ){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        PagedGridResult gridResult = myOrdersService.getOrdersTrend(userId,page,pageSize);
        return IMOOCJSONResult.ok(gridResult);
    }



    /**
     * 用于验证用户和订单是否有关联关系，避免非法调用
     *
     * @return
     */
    private IMOOCJSONResult checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok();
    }


}
