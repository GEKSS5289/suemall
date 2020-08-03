package com.sue.controller.usercentercontroller;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.pojo.Users;
import com.sue.pojo.dto.usercenterdto.CenterUserDTO;
import com.sue.service.usercenterservice.UserCenterService;
import com.sue.utils.CookieUtils;
import com.sue.utils.DateUtil;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.util.unit.DataUnit;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

        //定义头像保存的地址
        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String uploadPathPrefix = File.separator + userId;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
            if (file != null) {
                //获取文件上传名称
                String fileName = file.getOriginalFilename();

                if (StringUtils.isNotBlank(fileName)) {

                    //imooc.jpg -> ["imooc","jpg"]
                    String[] split = fileName.split("\\.");

                    //获取文件后缀
                    String suffix = split[split.length - 1];


                    if(!suffix.equalsIgnoreCase("png")&&
                            !suffix.equalsIgnoreCase("jpg")&&
                            !suffix.equalsIgnoreCase("jpeg")
                    ){
                        return IMOOCJSONResult.errorMsg("图片格式不正确");
                    }


                    //face-{userid}.jpg
                    //文件名重组
                    String newFileName = "face-" + userId + "." + suffix;

                    //上传头像最终保存地址
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                    //用于提供给web服务访问的地址
                    uploadPathPrefix += ("/"+newFileName);


                    //构建文件对象
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    //文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }


        String finalUserFaceUrl = "http://localhost:8088/foodie/faces" + uploadPathPrefix + "?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        //更新用户头像到数据库
        Users users = userCenterService.updateUserFace(userId, finalUserFaceUrl);

        setNullProperties(users);
        CookieUtils.setCookie(
                request,
                response,
                "user",
                JsonUtils.objectToJson(users),
                true
        );

        return IMOOCJSONResult.ok();
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
