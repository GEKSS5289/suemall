package com.sue.pojo.dto.usercenterdto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author sue
 * @date 2020/8/1 9:09
 */




/**
 * 用户对象 从客户端由用户传入的数据封装在此entity中
 */
@Data
public class CenterUserDTO {
    private String username;
    private String password;
    private String confirmPassword;
    @NotBlank(message = "用户昵称不能为空")
    @Length(max=12,message = "用户昵称不能超过12位")
    private String nickname;
    @Length(max=12,message = "用户真是姓名不能超过12位")
    private String realname;
    @Pattern(
            regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$",
            message = "手机号格式不正确"
    )
    private String mobile;
    @Email
    private String email;
    @Min(value = 0,message = "性别选择不正确")
    @Max(value = 2,message = "性别选择不正确")
    private Integer sex;
    private Date birthday;

}

