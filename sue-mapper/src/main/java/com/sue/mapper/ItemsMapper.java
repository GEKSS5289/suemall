package com.sue.mapper;

import com.sue.my.MyMapper;
import com.sue.pojo.Items;
import com.sue.pojo.vo.ItemCommentVO;
import com.sue.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapper extends MyMapper<Items> {
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap")Map<String,Object> map);

    public List<SearchItemsVO> searchItems(@Param("paramsMap")Map<String,Object> map);
    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap")Map<String,Object> map);

}