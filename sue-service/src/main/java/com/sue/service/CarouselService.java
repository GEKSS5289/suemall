package com.sue.service;

import com.sue.pojo.Carousel;

import java.util.Calendar;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:05
 */


public interface CarouselService {
    public List<Carousel> queryall(Integer isShow);
}