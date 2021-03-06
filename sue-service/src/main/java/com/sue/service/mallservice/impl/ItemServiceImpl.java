package com.sue.service.mallservice.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sue.enums.CommentLevel;
import com.sue.enums.YesOrNO;
import com.sue.mapper.*;
import com.sue.pojo.*;
import com.sue.pojo.vo.CommentLevelCountsVO;
import com.sue.pojo.vo.ItemCommentVO;
import com.sue.pojo.vo.SearchItemsVO;
import com.sue.pojo.vo.ShopCartVO;
import com.sue.service.mallservice.ItemService;
import com.sue.utils.DesensitizationUtil;
import com.sue.utils.PagedGridResult;
import com.sue.utils.PagedGridResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author sue
 * @date 2020/8/1 17:40
 */

@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemsMapper itemsMapper;

    @Resource
    private ItemsImgMapper itemsImgMapper;

    @Resource
    private ItemsSpecMapper itemsSpecMapper;

    @Resource
    private ItemsParamMapper itemsParamMapper;

    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;

    /**
     * 根据商品Id查询详情
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品Id查询商品图片列表
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsImgMapper.selectByExample(example);
    }

    /**
     * 根据商品Id查询商品规格
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    /**
     * 根据商品Id查询商品评价等级数量
     *
     * @param itemId
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {


        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.getType());
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.getType());
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.getType());
        Integer totalCount = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setBadCounts(badCounts);
        commentLevelCountsVO.setGoodCounts(goodCounts);
        commentLevelCountsVO.setNormalCounts(normalCounts);
        commentLevelCountsVO.setTotalCounts(totalCount);


        return commentLevelCountsVO;
    }

    /**
     * 根据商品id查询商品评价内容
     *
     * @param itemId
     * @param level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPageComments(
            String itemId,
            Integer level
            , Integer page
            , Integer pageSize
    ) {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("itemId", itemId);
        stringObjectHashMap.put("level", level);

        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> itemCommentVOS = itemsMapper.queryItemComments(stringObjectHashMap);

        itemCommentVOS.forEach(i -> {
            i.setNickname(DesensitizationUtil.commonDisplay(i.getNickname()));
        });

        return PagedGridResultUtils.setterPagedGrid(itemCommentVOS, page);
    }

    /**
     * 搜索商品列表
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("keywords", keywords);
        stringObjectHashMap.put("sort", sort);
        PageHelper.startPage(page, pageSize);

        List<SearchItemsVO> searchItemsVOS = itemsMapper.searchItems(stringObjectHashMap);

        return PagedGridResultUtils.setterPagedGrid(searchItemsVOS, page);
    }

    /**
     * 根据分类id搜索列表
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("catId", catId);
        stringObjectHashMap.put("sort", sort);
        PageHelper.startPage(page, pageSize);

        List<SearchItemsVO> searchItemsVOS = itemsMapper.searchItemsByThirdCat(stringObjectHashMap);

        return PagedGridResultUtils.setterPagedGrid(searchItemsVOS, page);
    }

    /**
     * 根据规格ids查询最新的购物车中商品数据 （用于刷新渲染购物车商品数据）
     *
     * @param specIds
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {
        String ids[] = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);

        return itemsMapper.queryItemsBySpecIds(specIdsList);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {

        ItemsComments comments = new ItemsComments();
        comments.setItemId(itemId);
        if (level != null) {
            comments.setCommentLevel(level);
        }

        return itemsCommentsMapper.selectCount(comments);
    }


    /**
     * 根据商品规格Id获取规格对象的具体信息
     *
     * @param specId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }


    /**
     * 根据商品id获取商品图片
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNO.YES.type);

        ItemsImg result = itemsImgMapper.selectOne(itemsImg);

        return result != null ? result.getUrl() : "";
    }


    /**
     * 扣减商品库存
     *
     * @param specId
     * @param buyCounts
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        //synchronized () 不推荐使用 集群下无用，性能低下
        //锁数据库 不推荐 导致数据库性能低下
        //分布式锁 zookeeper redis 两个都可以做分布式锁
        int result = itemsMapper.decreaseItemSpecStock(specId, buyCounts);

        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因库存不足");
        }


    }


}
