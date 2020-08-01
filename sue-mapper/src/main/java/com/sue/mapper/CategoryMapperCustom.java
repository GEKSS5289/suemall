package com.sue.mapper;

import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;
import com.sue.pojo.vo.NewItemsVO;
import com.sue.pojo.vo.SubCategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author sue
 * @date 2020/8/1 15:01
 */

public interface CategoryMapperCustom {
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String,Object> map);
}
