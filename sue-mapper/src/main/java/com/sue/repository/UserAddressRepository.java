package com.sue.repository;

import com.sue.pojo.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sue
 * @date 2020/7/31 13:44
 */

public interface UserAddressRepository extends JpaRepository<UserAddress,String> {
}
