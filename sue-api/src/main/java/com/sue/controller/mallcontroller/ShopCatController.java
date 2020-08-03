package com.sue.controller.mallcontroller;

import com.sue.pojo.dto.ShopcartDTO;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sue
 * @date 2020/8/2 11:10
 */

@Api(value = "购物车接口controller", tags = {"购物车接口相关api"})
@RestController
@RequestMapping("shopcart")
public class ShopCatController {

    public static final Logger log = LoggerFactory.getLogger(ShopCatController.class);

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartDTO shopcartDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }


        //TODO 前端用户在登录情况下 添加商品到购物车 会同时在后端同步购物车到redis缓存
        log.info("{}", shopcartDTO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestBody String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }


        //TODO 前端用户在登录情况下 添加商品到购物车 会同时在后端同步购物车到redis缓存

        return IMOOCJSONResult.ok();
    }
}
