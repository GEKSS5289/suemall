package com.sue.service.impl;

import com.sue.dto.UserDTO;
import com.sue.enums.Sex;
import com.sue.pojo.Users;
import com.sue.repository.UsersRepository;
import com.sue.service.UsersServie;
import com.sue.utils.CookieUtils;
import com.sue.utils.DateUtil;
import com.sue.utils.JsonUtils;
import com.sue.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;


@Service
public class UsersServieImpl implements UsersServie {


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private Sid sid;

    public static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    /**
     * 用户名是否存在
     *
     * @param username
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users byUsername = usersRepository.findByUsername(username);
        return byUsername==null?false:true;
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

        usersRepository.save(user);
        return user;
    }

    @Override
    public Users login(
            UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(userDTO.getUsername())&&StringUtils.isNotBlank(userDTO.getPassword())){
            if(userDTO.getPassword().length()<6){
                return null;
            }
            Users users = usersRepository
                    .findByUsernameAndPassword(userDTO.getUsername(),MD5Utils.getMD5Str(userDTO.getPassword()));

            if(users != null){
                this.setNullProperties(users);
                CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
                return users;
            }

        }
        return null;
    }

    @Override
    public void logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request,response,"user");
    }


    private void setNullProperties(Users users){
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
    }
}
