package com.sue.controller;

import com.sue.enums.PayMethod;
import com.sue.pojo.dto.SubmitOrderDTO;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sue
 * @date 2020/8/2 16:26
 */

@Api(value = "订单相关", tags = {"订单相关接口"})
@RequestMapping("orders")
@RestController
public class OrdersController {
    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderDTO submitOrderDTO) {

        if (submitOrderDTO.getPayMethod() != PayMethod.ALIPAY.getType() &&
                submitOrderDTO.getPayMethod() != PayMethod.ALIPAY.getType()) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }

        return IMOOCJSONResult.ok();
    }
}
