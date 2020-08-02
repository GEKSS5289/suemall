package com.sue.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sue
 * @date 2020/8/1 9:09
 */


@Data
@ApiModel(value = "用户对象DTO",description = "从c端传入的数据封装在此")
public class UserDTO {
    @ApiModelProperty(value = "用户名",name = "username",example = "sue",required = true)
    private String username;
    @ApiModelProperty(value = "密码",name = "password",example = "sue",required = true)
    private String password;
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "sue",required = true)
    private String confirmPassword;
}
