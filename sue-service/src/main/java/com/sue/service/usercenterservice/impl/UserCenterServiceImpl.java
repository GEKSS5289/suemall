package com.sue.service.usercenterservice.impl;

import com.sue.mapper.UsersMapper;
import com.sue.pojo.Users;
import com.sue.pojo.dto.usercenterdto.CenterUserDTO;
import com.sue.service.usercenterservice.UserCenterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sue
 * @date 2020/8/3 13:26
 */

@Service
public class UserCenterServiceImpl implements UserCenterService {



    @Resource
    private UsersMapper usersMapper;


    /**
     * 根据userId查询用户信息
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {

        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;

    }


    /**
     * 修改用户信息
     * @param userId
     * @param centerUserDTO
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserDTO centerUserDTO) {

        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserDTO,updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);

        Users users = queryUserInfo(userId);

        return users;

    }


}
