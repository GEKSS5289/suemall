package com.sue.service.mallservice;

import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;
import com.sue.pojo.vo.NewItemsVO;
import com.sue.pojo.vo.SubCategoryVO;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:37
 */

public interface CategoryService {

    public List<Category> queryAllRootLevelCat();

    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下的6条最新数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);

}
