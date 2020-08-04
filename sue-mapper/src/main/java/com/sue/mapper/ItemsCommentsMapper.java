package com.sue.mapper;

import com.sue.my.MyMapper;
import com.sue.pojo.ItemsComments;
import com.sue.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {

    public void saveComments(Map<String,Object> map);

    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String,Object> map);
}