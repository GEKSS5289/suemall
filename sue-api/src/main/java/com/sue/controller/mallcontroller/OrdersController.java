package com.sue.controller.mallcontroller;

import com.sue.core.util.PayCenterDataUtils;
import com.sue.enums.OrderStatusEnum;
import com.sue.enums.PayMethod;
import com.sue.exception.mallexception.OrdersException;
import com.sue.pojo.OrderStatus;
import com.sue.pojo.dto.malldto.SubmitOrderDTO;
import com.sue.pojo.vo.MerchantOrdersVO;
import com.sue.pojo.vo.OrderVO;
import com.sue.service.mallservice.OrderService;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;



    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderDTO submitOrderDTO, HttpServletRequest request, HttpServletResponse response) {

        if (submitOrderDTO.getPayMethod() != PayMethod.WEIXIN.getType() &&
                submitOrderDTO.getPayMethod() != PayMethod.ALIPAY.getType()) {
            throw new OrdersException(20000);
        }

        //创建订单
        OrderVO order = orderService.createOrder(submitOrderDTO);
        String orderId = order.getOrderId();


//        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);

        if(!PayCenterDataUtils.sendPayCenter(order,restTemplate)){
            throw new OrdersException(20001);
        }


        return IMOOCJSONResult.ok(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){

        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }


    @PostMapping("/getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatus);
    }

}
