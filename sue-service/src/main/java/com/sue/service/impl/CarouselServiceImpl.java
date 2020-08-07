package com.sue.service.impl;

import com.sue.pojo.Carousel;
import com.sue.repository.CarouselRepository;
import com.sue.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/7 18:25
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselRepository carouselRepository;

    /***
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    @Override
    public List<Carousel> queryAll(Integer isShow) {
        return carouselRepository.findAllByIsShow(isShow);
    }
}
