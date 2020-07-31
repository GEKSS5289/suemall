package com.sue.repository;

import com.sue.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sue
 * @date 2020/7/31 13:37
 */

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
