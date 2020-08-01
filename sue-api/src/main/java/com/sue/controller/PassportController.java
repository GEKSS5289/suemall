package com.sue.controller;

import com.sue.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;



    @GetMapping("/usernameIsExist")
    public int usernameIsExist(@RequestParam String username){


        if(StringUtils.isBlank(username)){
            return 500;
        }

        //查找注册的用户名是否存在
        if(userService.queryUsernameIsExist(username)){
            return 500;
        }

        return 200;

    }









}
