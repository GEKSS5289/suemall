package com.sue.controller.mallcontroller;

import com.sue.enums.YesOrNO;
import com.sue.exception.commonException.DataNullException;
import com.sue.pojo.Carousel;
import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;
import com.sue.service.mallservice.CarouselService;
import com.sue.service.mallservice.CategoryService;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import com.sue.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@Api(value = "首页", tags = {"首页相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);


    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;



    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图列表", notes = "获取轮播图列表", httpMethod = "GET")
    public IMOOCJSONResult carousel() {

        String carousel = redisOperator.get("carousel");
        List<Carousel> queryall = new ArrayList<>();
        if(StringUtils.isBlank(carousel)){
           queryall = carouselService.queryall(YesOrNO.YES.type);
           redisOperator.set("carousel", JsonUtils.objectToJson(queryall));
        }else{
           queryall = JsonUtils.jsonToList(carousel,Carousel.class);
        }
        return IMOOCJSONResult.ok(queryall);
    }



    @ApiOperation(value = "获取商品分类(1级)", notes = "获取商品分类(1级)", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {

        String cats = redisOperator.get("cats");
        List<Category> categories = new ArrayList<>();
        if(StringUtils.isBlank(cats)){
          categories = categoryService.queryAllRootLevelCat();
          redisOperator.set("cats",JsonUtils.objectToJson(categories));
        }else{
            categories = JsonUtils.jsonToList(cats,Category.class);
        }


        return IMOOCJSONResult.ok(categories);
    }


    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId
    ) {

        if (rootCatId == null) {
            throw new DataNullException(44400);
        }
        String subCats = redisOperator.get("subcats:"+rootCatId);
        List<CategoryVO> subCatList = new ArrayList<>();
        if(StringUtils.isBlank(subCats)){
            subCatList = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subcats:"+rootCatId,JsonUtils.objectToJson(subCatList));
        }else{
            subCatList = JsonUtils.jsonToList(subCats,CategoryVO.class);
        }
        return IMOOCJSONResult.ok(subCatList);
    }


    @ApiOperation(value = "查询每个一级分类下的6条最新商品数据", notes = "查询每个一级分类下的6条最新商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable Integer rootCatId
    ) {

        if (rootCatId == null) {
            throw new DataNullException(44400);
        }

        return IMOOCJSONResult.ok(categoryService.getSixNewItemsLazy(rootCatId));
    }


}
