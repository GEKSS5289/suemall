package com.sue.service;

import org.springframework.stereotype.Repository;

/**
 * @author sue
 * @date 2020/8/1 8:38
 */

public interface UserService {
    /**
     * 判断用户是否存在
     */
    public boolean queryUsernameIsExist(String username);
}
