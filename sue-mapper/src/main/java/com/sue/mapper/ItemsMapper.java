package com.sue.mapper;

import com.sue.my.MyMapper;
import com.sue.pojo.Items;
import com.sue.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapper extends MyMapper<Items> {
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap")Map<String,Object> map);
}