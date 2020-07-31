package com.sue.repository;

import com.sue.pojo.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sue
 * @date 2020/7/31 13:43
 */

public interface OrderStatusRepository extends JpaRepository<OrderStatus,String> {
}
