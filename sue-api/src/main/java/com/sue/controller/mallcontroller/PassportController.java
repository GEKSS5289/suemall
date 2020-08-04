package com.sue.controller.mallcontroller;

import com.sue.pojo.Users;
import com.sue.pojo.dto.malldto.UserDTO;
import com.sue.service.mallservice.UserService;
import com.sue.utils.CookieUtils;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import com.sue.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@Api(value = "注册登录", tags = {"用于相关接口"})
@RestController
@RequestMapping("passport")
@Validated
public class PassportController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {


        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }

        //查找注册的用户名是否存在
        if (userService.queryUsernameIsExist(username)) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        return IMOOCJSONResult.ok();

    }


    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult createUser(
            @RequestBody UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (
                StringUtils.isNotBlank(userDTO.getUsername()) &&
                        StringUtils.isNotBlank(userDTO.getPassword()) &&
                        StringUtils.isNotBlank(userDTO.getConfirmPassword())
        ) {
            if (userService.queryUsernameIsExist(userDTO.getUsername())) {
                return IMOOCJSONResult.errorMsg("已经存在该用户");
            }
            if (userDTO.getPassword().length() < 6) {
                return IMOOCJSONResult.errorMsg("密码长度不能小于6");
            }
            if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                return IMOOCJSONResult.errorMsg("两次密码不一致");
            }

            Users user = userService.createUser(userDTO);

            this.setNullProperties(user);
            CookieUtils.setCookie(
                    request,
                    response,
                    "user",
                    JsonUtils.objectToJson(user),
                    true
            );

            return IMOOCJSONResult.ok();
        }
        return IMOOCJSONResult.errorMsg("有空字段");
    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(
            @Valid @RequestBody UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {


        Users users = userService.queryUserForLogin(
                userDTO.getUsername(),
                MD5Utils.getMD5Str(userDTO.getPassword())
        );

        if (users == null) {
            return IMOOCJSONResult.errorMsg("出现异常");
        }

        this.setNullProperties(users);
        CookieUtils.setCookie(
                request,
                response,
                "user",
                JsonUtils.objectToJson(users),
                true
        );

        return IMOOCJSONResult.ok(users);

    }


    public void setNullProperties(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
    }


    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        //清除用户相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");
        return IMOOCJSONResult.ok();
    }


}