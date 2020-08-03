package com.sue.service.mallservice;

import com.sue.pojo.Carousel;
import com.sue.pojo.vo.CategoryVO;

import java.util.Calendar;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/1 14:05
 */


public interface CarouselService {
    public List<Carousel> queryall(Integer isShow);



}
