package com.sue.service.impl;

import com.sue.enums.Sex;
import com.sue.mapper.UsersMapper;
import com.sue.pojo.Users;
import com.sue.pojo.dto.UserDTO;
import com.sue.service.UserService;
import com.sue.utils.DateUtil;
import com.sue.utils.MD5Utils;
import org.apache.catalina.User;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sue
 * @date 2020/8/1 8:39
 */
@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    public static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";





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







    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserDTO userDTO) {


        String userId = sid.nextShort();

        Users user = new Users();
        user.setUsername(userDTO.getUsername());
        user.setPassword(MD5Utils.getMD5Str(userDTO.getPassword()));
        user.setNickname(userDTO.getUsername());
        user.setFace(USER_FACE);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.getType());
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        user.setId(userId);

        usersMapper.insert(user);

        return user;
    }


}
