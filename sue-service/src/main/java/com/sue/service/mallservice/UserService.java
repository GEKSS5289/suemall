package com.sue.service.mallservice;

import com.sue.pojo.Users;
import com.sue.pojo.dto.malldto.UserDTO;

/**
 * @author sue
 * @date 2020/8/1 8:38
 */

public interface UserService {
    /**
     * 判断用户是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     *
     * @param userDTO
     * @return
     */
    public Users createUser(UserDTO userDTO);

    /**
     * 检索用户名和密码是否匹配
     */
    public Users queryUserForLogin(String username, String password);

}
