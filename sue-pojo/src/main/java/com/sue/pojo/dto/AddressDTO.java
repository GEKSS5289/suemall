package com.sue.pojo.dto;

import lombok.Data;

/**
 * @author sue
 * @date 2020/8/2 15:16
 */
@Data
public class AddressDTO {
    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
