package com.sue.controller;

import com.sue.pojo.Carousel;
import com.sue.service.CarouselService;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.YesNoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/7 18:24
 */
@RestController
@RequestMapping("index")
@Api(value = "首页",tags = {"首页展示的相关接口"})
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "获取轮播图列表",notes = "获取轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel(){
        List<Carousel> carousels = carouselService.queryAll(1);
        return IMOOCJSONResult.ok(carousels);
    }

}
