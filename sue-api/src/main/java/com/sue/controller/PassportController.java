package com.sue.controller;

import com.sue.dto.UserDTO;
import com.sue.service.UsersServie;
import com.sue.utils.IMOOCJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UsersServie usersServie;

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
