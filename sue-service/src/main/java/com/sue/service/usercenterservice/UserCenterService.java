package com.sue.service.usercenterservice;

import com.sue.pojo.Users;
import com.sue.pojo.dto.usercenterdto.CenterUserDTO;

/**
 * @author sue
 * @date 2020/8/3 13:25
 */

public interface UserCenterService {
    /**
     * 根据userId查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserDTO
     * @return
     */
    public Users updateUserInfo(String userId, CenterUserDTO centerUserDTO);

}
