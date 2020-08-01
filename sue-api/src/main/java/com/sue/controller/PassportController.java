package com.sue.controller;

import com.sue.pojo.dto.UserDTO;
import com.sue.service.UserService;
import com.sue.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@Api(value = "注册登录",tags = {"用于相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;





    @ApiOperation(value = "用户名是否存在",notes="用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {


        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMap("用户名不能为空");
        }

        //查找注册的用户名是否存在
        if (userService.queryUsernameIsExist(username)) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        return IMOOCJSONResult.ok();

    }







    @ApiOperation(value = "用户注册",notes="用户注册",httpMethod = "POST")
    @PostMapping("/register")
    public IMOOCJSONResult createUser(@RequestBody UserDTO userDTO) {

        if (
                StringUtils.isNotBlank(userDTO.getUsername())&&
                        StringUtils.isNotBlank(userDTO.getPassword()) &&
                        StringUtils.isNotBlank(userDTO.getConfirmPassword())
        ){
            if(userService.queryUsernameIsExist(userDTO.getUsername())){
                return  IMOOCJSONResult.errorMap("已经存在该用户");
            }
            if(userDTO.getPassword().length()<6){
                return  IMOOCJSONResult.errorMap("密码长度不能小于6");
            }
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
                return IMOOCJSONResult.errorMap("两次密码不一致");
            }
            userService.createUser(userDTO);
            return IMOOCJSONResult.ok();
        }
        return IMOOCJSONResult.errorMap("有空字段");
    }


}
