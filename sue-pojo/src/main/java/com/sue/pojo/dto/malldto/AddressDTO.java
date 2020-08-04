package com.sue.pojo.dto.malldto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author sue
 * @date 2020/8/2 15:16
 */
@Data
public class AddressDTO {
    private String addressId;
    private String userId;
    @NotBlank
    @Length(max = 12)
    private String receiver;
    @NotBlank
    @Length(max = 12)
    @Pattern(
            regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$",
            message = "手机号格式不正确"
    )
    private String mobile;
    @NotBlank(message = "省份不能为空")
    private String province;
    @NotBlank(message = "城市不能为空")
    private String city;
    @NotBlank(message = "区不能为空")
    private String district;
    @NotBlank(message = "地址详情不能为空")
    private String detail;
}
