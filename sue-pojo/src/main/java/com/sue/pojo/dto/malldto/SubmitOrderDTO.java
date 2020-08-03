package com.sue.pojo.dto.malldto;

import lombok.Data;

/**
 * @author sue
 * @date 2020/8/2 16:27
 */
@Data
public class SubmitOrderDTO {
    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
    private String token;
}
