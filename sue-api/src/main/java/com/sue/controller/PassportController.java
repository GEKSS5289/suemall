package com.sue.controller;

import com.sue.dto.UserDTO;
import com.sue.pojo.Users;
import com.sue.service.UsersServie;
import com.sue.utils.CookieUtils;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */
@Api(value = "用户接口",tags = {"关于用户相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UsersServie usersServie;





    @ApiOperation(value = "用户是否存在",tags = {"用户是否存在"},httpMethod ="GET" )
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        if(usersServie.queryUsernameIsExist(username)){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
        return IMOOCJSONResult.ok();
    }






    @ApiOperation(value = "用户注册",tags = {"用户注册"},httpMethod ="POST" )
    @PostMapping("/regist")
    public IMOOCJSONResult register(
            @RequestBody UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response){
        if(StringUtils.isNotBlank(userDTO.getUsername())&&
        StringUtils.isNotBlank(userDTO.getPassword())&&
        StringUtils.isNotBlank(userDTO.getConfirmPassword())){
            if(usersServie.queryUsernameIsExist(userDTO.getUsername())){
                return IMOOCJSONResult.errorMsg("已经存在该用户");
            }
            if(userDTO.getPassword().length()<6){
                return IMOOCJSONResult.errorMsg("密码不能少于6位");
            }
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
                return IMOOCJSONResult.errorMsg("两次密码不一致");
            }
            Users user = usersServie.createUser(userDTO);
            CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(user),true);
            return IMOOCJSONResult.ok();
        }
        return IMOOCJSONResult.errorMsg("所有参数不可为空");
    }



    @ApiOperation(value = "用户登录",notes = "用户登陆",httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(
            @RequestBody UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws UnsupportedEncodingException {
        Users login = usersServie.login(userDTO, request, response);
        return login!=null?IMOOCJSONResult.ok(login):IMOOCJSONResult.errorMsg("登陆失败");
    }

    @ApiOperation(value = "用户退出",notes = "用户退出",httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult login(
            @RequestBody String userId,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        usersServie.logout(userId,request,response);
        return IMOOCJSONResult.ok();
    }





}
