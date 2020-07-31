package com.sue.repository;

import com.sue.pojo.Items;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sue
 * @date 2020/7/31 13:38
 */

public interface ItemsRepository extends JpaRepository<Items,String> {
}
