package com.sue.service.impl;

import com.sue.mapper.CarouselMapper;
import com.sue.pojo.Carousel;
import com.sue.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:06
 */

@Service
public class CarouselServiceImpl implements CarouselService {


    @Resource
    private CarouselMapper carouselMapper;




    @Override
    public List<Carousel> queryall(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("is_show",isShow);
        return carouselMapper.selectByExample(example);
    }




}
