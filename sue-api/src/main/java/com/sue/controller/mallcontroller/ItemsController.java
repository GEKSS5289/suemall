package com.sue.controller.mallcontroller;

import com.sue.exception.commonException.DataNullException;
import com.sue.pojo.Items;
import com.sue.pojo.ItemsImg;
import com.sue.pojo.ItemsParam;
import com.sue.pojo.ItemsSpec;
import com.sue.pojo.vo.CommentLevelCountsVO;
import com.sue.pojo.vo.ItemInfoVO;
import com.sue.service.mallservice.ItemService;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.PagedGridResult;
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

@Api(value = "商品接口", tags = {"商品信息展示接口"})
@RequestMapping("items")
@RestController
public class ItemsController {


    @Autowired
    private ItemService itemService;


    @ApiOperation(value = "商品信息", notes = "商品信息", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult itemInfo(
            @ApiParam(value = "itemId", name = "商品ID", required = true)
            @PathVariable String itemId
    ) {

        if (StringUtils.isBlank(itemId)) {
           throw new DataNullException(44400);
        }

        Items items = itemService.queryItemById(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO(items, itemsImgs, itemsSpecs, itemsParam);


        return IMOOCJSONResult.ok(itemInfoVO);
    }


    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(value = "itemId", name = "商品ID", required = true)
            @RequestParam String itemId
    ) {

        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg(null);
        }

        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);

        return IMOOCJSONResult.ok(commentLevelCountsVO);
    }


    @ApiOperation(value = "查询商品评价内容", notes = "查询商品评价内容", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(value = "itemId", name = "商品ID", required = true)
            @RequestParam String itemId,
            @ApiParam(value = "level", name = "商品评价级别", required = true)
            @RequestParam Integer level,
            @ApiParam(value = "page", name = "查询下一页的第几页", required = true)
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @ApiParam(value = "pageSize", name = "每页显示条数", required = true)
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {

        if (StringUtils.isBlank(itemId)) {
            throw new DataNullException(44400);
        }

        PagedGridResult pagedGridResult = itemService.queryPageComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }


    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult search(
            @ApiParam(value = "keywords", name = "查询关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(value = "sort", name = "排序", required = true)
            @RequestParam String sort,
            @ApiParam(value = "page", name = "查询下一页的第几页", required = true)
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @ApiParam(value = "pageSize", name = "每页显示条数", required = true)
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {

        if (StringUtils.isBlank(keywords) || page == 0 || pageSize == 0) {
            throw new DataNullException(44400);
        }
        System.out.println(pageSize);
        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }


    @ApiOperation(value = "通过分类Id搜索商品列表", notes = "通过分类Id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(value = "catId", name = "分类Id", required = true)
            @RequestParam Integer catId,
            @ApiParam(value = "sort", name = "排序", required = true)
            @RequestParam String sort,
            @ApiParam(value = "page", name = "查询下一页的第几页", required = true)
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @ApiParam(value = "pageSize", name = "每页显示条数", required = true)
            @RequestParam(defaultValue = "10", required = false) Integer pageSize
    ) {

        if (catId == null || page == 0 || pageSize == 0) {
            throw new DataNullException(44400);
        }

        PagedGridResult pagedGridResult = itemService.searchItems(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }


    @ApiOperation(value = "通过商品规格ids查询最新商品数据", notes = "通过商品规格ids查询最新商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(
            @ApiParam(value = "itemSpecIds", name = "拼接规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds
    ) {

        if (StringUtils.isBlank(itemSpecIds)) {
            throw new DataNullException(44400);
        }

        return IMOOCJSONResult.ok(itemService.queryItemsBySpecIds(itemSpecIds));
    }


}
