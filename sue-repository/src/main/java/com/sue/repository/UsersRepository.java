package com.sue.repository;

import com.sue.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sue
 * @date 2020/7/31 13:44
 */

public interface UsersRepository extends JpaRepository<Users,String> {
    Users findByUsername(String username);
    Users findByUsernameAndPassword(String username,String password);
}
