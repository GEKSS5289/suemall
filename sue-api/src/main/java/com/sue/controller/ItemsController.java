package com.sue.controller;

import com.sue.pojo.Items;
import com.sue.pojo.ItemsImg;
import com.sue.pojo.ItemsParam;
import com.sue.pojo.ItemsSpec;
import com.sue.pojo.vo.CommentLevelCountsVO;
import com.sue.pojo.vo.ItemInfoVO;
import com.sue.service.ItemService;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 17:48
 */

@Api(value = "商品接口",tags = {"商品信息展示接口"})
@RequestMapping("items")
@RestController
public class ItemsController {


    @Autowired
    private ItemService itemService;



    @ApiOperation(value = "商品信息",notes = "商品信息",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult itemInfo(
            @ApiParam(value = "itemId",name = "商品ID",required = true)
            @PathVariable String itemId
            ){

        if(StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg(null);
        }

        Items items = itemService.queryItemById(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO(items, itemsImgs, itemsSpecs, itemsParam);


        return IMOOCJSONResult.ok(itemInfoVO);
    }







    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(value = "itemId",name = "商品ID",required = true)
            @RequestParam String itemId
    ){

        if(StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg(null);
        }

        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);

        return IMOOCJSONResult.ok(commentLevelCountsVO);
    }









}
