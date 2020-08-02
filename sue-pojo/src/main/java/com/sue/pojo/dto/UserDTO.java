package com.sue.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author sue
 * @date 2020/8/1 9:09
 */


@Data
@ApiModel(value = "用户对象DTO",description = "从c端传入的数据封装在这里")
public class UserDTO {


    @ApiModelProperty(value = "用户名",name = "username",example = "sue",required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;


    @ApiModelProperty(value = "密码",name = "password",example = "sue",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;


    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "sue",required = false)
    private String confirmPassword;
}
