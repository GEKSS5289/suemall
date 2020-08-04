package com.sue.service.mallservice.impl;

import com.sue.mapper.CarouselMapper;
import com.sue.pojo.Carousel;
import com.sue.service.mallservice.CarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:06
 */

@Service
public class CarouselServiceImpl implements CarouselService {


    @Resource
    private CarouselMapper carouselMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryall(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);
        return carouselMapper.selectByExample(example);
    }


}
