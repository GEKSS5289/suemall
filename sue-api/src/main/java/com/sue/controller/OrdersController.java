package com.sue.controller;

import com.sue.enums.OrderStatusEnum;
import com.sue.enums.PayMethod;
import com.sue.pojo.dto.SubmitOrderDTO;
import com.sue.pojo.vo.MerchantOrdersVO;
import com.sue.pojo.vo.OrderVO;
import com.sue.service.OrderService;
import com.sue.utils.CookieUtils;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sue
 * @date 2020/8/2 16:26
 */

@Api(value = "订单相关", tags = {"订单相关接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;



    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderDTO submitOrderDTO, HttpServletRequest request, HttpServletResponse response) {

        if (submitOrderDTO.getPayMethod() != PayMethod.WEIXIN.getType() &&
                submitOrderDTO.getPayMethod() != PayMethod.ALIPAY.getType()) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }

        //创建订单
        OrderVO order = orderService.createOrder(submitOrderDTO);
        String orderId = order.getOrderId();
        MerchantOrdersVO merchantOrdersVO = order.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);


//        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);

        //向支付中心发送当前订单，用于保存支付中心订单数据
        return IMOOCJSONResult.ok(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){

        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);


        return HttpStatus.OK.value();
    }
}