package com.sue.pojo.vo;

import lombok.Data;

/**
 * @author sue
 * @date 2020/8/1 15:14
 */

@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
