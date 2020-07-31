package com.sue.repository;

import com.sue.pojo.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sue
 * @date 2020/7/31 13:42
 */

public interface OrderItemsRepository extends JpaRepository<OrderItems,String> {
}
