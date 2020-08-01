package com.sue.pojo.dto;

import lombok.Data;

/**
 * @author sue
 * @date 2020/8/1 9:09
 */


@Data
public class UserDTO {
    private String username;
    private String password;
    private String confirmPassword;
}
