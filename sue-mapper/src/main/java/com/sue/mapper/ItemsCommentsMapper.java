package com.sue.mapper;

import com.sue.pojo.ItemsComments;

public interface ItemsCommentsMapper {
    int deleteByPrimaryKey(String id);

    int insert(ItemsComments record);

    int insertSelective(ItemsComments record);

    ItemsComments selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ItemsComments record);

    int updateByPrimaryKey(ItemsComments record);
}