package com.sue.controller.usercentercontroller;

import com.sue.pojo.Users;
import com.sue.pojo.dto.usercenterdto.CenterUserDTO;
import com.sue.service.usercenterservice.UserCenterService;
import com.sue.utils.CookieUtils;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author sue
 * @date 2020/8/3 13:44
 */

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("/userInfo")
@Validated
public class CenterUserController {


    @Autowired
    private UserCenterService userCenterService;


    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @Valid @RequestBody CenterUserDTO centerUserDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Users users = userCenterService.updateUserInfo(userId, centerUserDTO);
        setNullProperties(users);
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

}
