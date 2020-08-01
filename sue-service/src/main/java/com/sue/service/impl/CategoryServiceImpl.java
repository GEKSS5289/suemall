package com.sue.service.impl;

import com.sue.mapper.CategoryMapper;
import com.sue.mapper.CategoryMapperCustom;
import com.sue.pojo.Category;
import com.sue.pojo.vo.CategoryVO;
import com.sue.pojo.vo.SubCategoryVO;
import com.sue.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:38
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryMapperCustom categoryMapperCustom;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {

        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        //查找根节点
        criteria.andEqualTo("type",1);
        return categoryMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {

        return categoryMapperCustom.getSubCatList(rootCatId);
    }
}
