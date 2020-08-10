package com.sue.controller.usercentercontroller;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.core.util.ClearDataUtils;
import com.sue.core.util.FileUtils;
import com.sue.pojo.Users;
import com.sue.pojo.dto.usercenterdto.CenterUserDTO;
import com.sue.pojo.vo.UsersVO;
import com.sue.service.usercenterservice.UserCenterService;
import com.sue.utils.CookieUtils;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import com.sue.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

/**
 * @author sue
 * @date 2020/8/3 13:44
 */

@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("/userInfo")
@Validated
public class UserCenterController extends BaseController {


    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private RedisOperator redisOperator;


    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult userInfo(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @Valid @RequestBody CenterUserDTO centerUserDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Users users = userCenterService.updateUserInfo(userId, centerUserDTO);
//        ClearDataUtils.setNullProperties(users);
        UsersVO usersVO = conventUsersVO(users);

        CookieUtils.setCookie(
                request,
                response,
                "user",
                JsonUtils.objectToJson(usersVO),
                true
        );



        return IMOOCJSONResult.ok(users);
    }


    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response
    ) {


        String finalUserFaceUrl = FileUtils.saveFile(file, userId);

        Users users = userCenterService.updateUserFace(userId, finalUserFaceUrl);

//        ClearDataUtils.setNullProperties(users);

        UsersVO usersVO = conventUsersVO(users);


        CookieUtils.setCookie(
                request,
                response,
                "user",
                JsonUtils.objectToJson(usersVO),
                true
        );

        return IMOOCJSONResult.ok();
    }


    private UsersVO conventUsersVO(Users user){
        //实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();

        redisOperator.set(REDIS_USER_TOKEN+":"+user.getId(),uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }


}
