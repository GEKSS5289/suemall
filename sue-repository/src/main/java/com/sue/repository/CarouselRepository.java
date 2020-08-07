package com.sue.repository;

import com.sue.pojo.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sue
 * @date 2020/7/31 13:36
 */


public interface CarouselRepository extends JpaRepository<Carousel,String> {
    List<Carousel> findAllByIsShow(Integer isShow);
}
