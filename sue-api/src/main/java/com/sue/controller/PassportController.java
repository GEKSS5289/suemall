package com.sue.controller;

import com.sue.dto.UserDTO;
import com.sue.service.UsersServie;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/register")
    public IMOOCJSONResult register(@RequestBody UserDTO userDTO){
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
            usersServie.createUser(userDTO);
            return IMOOCJSONResult.ok();
        }
        return IMOOCJSONResult.errorMsg("所有参数不可为空");
    }


}
