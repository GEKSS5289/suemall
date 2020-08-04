package com.sue.controller.usercentercontroller;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.pojo.Users;
import com.sue.pojo.dto.usercenterdto.CenterUserDTO;
import com.sue.service.usercenterservice.MyOrdersService;
import com.sue.service.usercenterservice.UserCenterService;
import com.sue.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public IMOOCJSONResult comments(
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
            return IMOOCJSONResult.errorMsg(null);
        }

        PagedGridResult pagedGridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }




}
