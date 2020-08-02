package com.sue.pojo.dto;

import lombok.Data;

/**
 * @author sue
 * @date 2020/8/2 11:13
 */
@Data
public class ShopcartDTO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private Integer buyCounts;
    private String priceDiscount;
    private String priceNormal;
}
