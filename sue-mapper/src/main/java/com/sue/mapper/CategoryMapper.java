package com.sue.mapper;

import com.sue.my.MyMapper;
import com.sue.pojo.Carousel;
import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}