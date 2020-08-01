package com.sue.mapper;

import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;
import com.sue.pojo.vo.SubCategoryVO;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 15:01
 */

public interface CategoryMapperCustom {
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}
