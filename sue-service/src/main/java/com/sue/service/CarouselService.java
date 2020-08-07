package com.sue.service;

import com.sue.pojo.Carousel;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/7 18:25
 */

public interface CarouselService {

    /***
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    List<Carousel> queryAll(Integer isShow);
}
