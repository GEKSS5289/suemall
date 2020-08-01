package com.sue.service;

import com.sue.pojo.Category;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:37
 */

public interface CategoryService {

    public List<Category> queryAllRootLevelCat();

}
