package com.sue.service.impl;

import com.sue.enums.CommentLevel;
import com.sue.mapper.*;
import com.sue.pojo.*;
import com.sue.pojo.vo.CommentLevelCountsVO;
import com.sue.pojo.vo.ItemCommentVO;
import com.sue.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        criteria.andEqualTo("itemId",itemId);

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
        criteria.andEqualTo("itemId",itemId);
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
        criteria.andEqualTo("itemId",itemId);
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
        Integer totalCount = goodCounts+normalCounts+badCounts;

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
    public List<ItemCommentVO> queryPageComments(String itemId, Integer level) {
        Map<String,Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("itemId",itemId);
        stringObjectHashMap.put("level",level);
        return itemsMapper.queryItemComments(stringObjectHashMap);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){

        ItemsComments comments = new ItemsComments();
        comments.setItemId(itemId);
        if(level != null){
            comments.setCommentLevel(level);
        }

        return  itemsCommentsMapper.selectCount(comments);
    }


}
