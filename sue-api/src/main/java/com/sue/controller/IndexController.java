package com.sue.controller;

import com.sue.enums.YesOrNO;
import com.sue.pojo.Carousel;
import com.sue.pojo.Users;
import com.sue.pojo.dto.UserDTO;
import com.sue.service.CarouselService;
import com.sue.service.UserService;
import com.sue.utils.CookieUtils;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import com.sue.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@Api(value = "首页",tags = {"首页相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);


    @Autowired
    private CarouselService carouselService;



    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图列表",notes = "获取轮播图列表",httpMethod = "GET")
    public IMOOCJSONResult carousel(){
        return IMOOCJSONResult.ok(carouselService.queryall(YesOrNO.YES.type));
    }







}
