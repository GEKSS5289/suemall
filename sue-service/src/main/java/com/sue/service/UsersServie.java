package com.sue.service;

import com.sue.dto.UserDTO;
import com.sue.pojo.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface UsersServie {
    /**
     * 用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    public Users createUser(UserDTO userDTO);

    public Users login(
            UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException;

    public void logout(String userId,
                       HttpServletRequest request,
                       HttpServletResponse response);
}
