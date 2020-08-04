package com.sue.controller.usercentercontroller;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.enums.YesOrNO;
import com.sue.pojo.OrderItems;
import com.sue.pojo.Orders;
import com.sue.pojo.dto.usercenterdto.OrderItemsCommentDTO;
import com.sue.service.usercenterservice.MyCommentService;
import com.sue.service.usercenterservice.MyOrdersService;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/3 13:44
 */

@Api(value = "用户中心评价模块", tags = {"用户中心评价模块接口"})
@RestController
@RequestMapping("/mycomments")
@Validated
public class MyCommentController extends BaseController {


    @Autowired
    private MyCommentService myCommentService;

    @Autowired
    private MyOrdersService myOrdersService;


    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult comments(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId

    ) {


        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if(imoocjsonResult.getStatus() != HttpStatus.OK.value()){
            return imoocjsonResult;
        }
        Orders data = (Orders)imoocjsonResult.getData();
        if(data.getIsComment() == YesOrNO.YES.type){
            return IMOOCJSONResult.errorMsg("该订单已经评论过了");
        }

        List<OrderItems> orderItems = myCommentService.queryPendingComment(orderId);


        return IMOOCJSONResult.ok(orderItems);
    }



    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId,
            @RequestParam List<OrderItemsCommentDTO> commentList

    ) {
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if(imoocjsonResult.getStatus() != HttpStatus.OK.value()){
            return imoocjsonResult;
        }
        if(commentList == null || commentList.isEmpty() || commentList.size() == 0){
            return IMOOCJSONResult.errorMsg("评论内容不能为空");
        }

        myCommentService.saveComments(orderId,userId,commentList);

        return IMOOCJSONResult.ok();
    }



    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = true)
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @ApiParam(name = "pageSize", value = "每页显示条数", required = true)
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg(null);
        }

        PagedGridResult pagedGridResult = myCommentService.queryMyComments(userId, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult);
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
        return IMOOCJSONResult.ok(orders);
    }


}
