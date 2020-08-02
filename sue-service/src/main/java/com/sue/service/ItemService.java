package com.sue.service;

import com.sue.pojo.Items;
import com.sue.pojo.ItemsImg;
import com.sue.pojo.ItemsParam;
import com.sue.pojo.ItemsSpec;
import com.sue.pojo.vo.CommentLevelCountsVO;
import com.sue.pojo.vo.ItemCommentVO;
import com.sue.utils.PagedGridResult;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 17:36
 */

public interface ItemService {

    /**
     * 根据商品Id查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品Id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);


    /**
     * 根据商品Id查询商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);


    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);


    /**
     * 根据商品Id查询商品评价等级数量
     * @param itemId
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);


    /**
     * 根据商品id查询商品评价内容
     * @param itemId
     * @param level
     * @return
     */
    public PagedGridResult queryPageComments(String itemId, Integer level
            , Integer page
            , Integer pageSize);


    /**
     * 搜索商品列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords,String sort,Integer page,Integer pageSize);
}
