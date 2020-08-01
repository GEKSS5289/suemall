package com.sue.service.impl;

import com.sue.mapper.UsersMapper;
import com.sue.pojo.Users;
import com.sue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @author sue
 * @date 2020/8/1 8:39
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;


    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        Users users = usersMapper.selectOneByExample(userExample);


        return users == null ? false : true;

    }





}
