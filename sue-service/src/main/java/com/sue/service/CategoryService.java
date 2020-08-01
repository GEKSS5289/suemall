package com.sue.service;

import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;
import com.sue.pojo.vo.SubCategoryVO;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:37
 */

public interface CategoryService {

    public List<Category> queryAllRootLevelCat();

    public List<CategoryVO> getSubCatList(Integer rootCatId);

}
