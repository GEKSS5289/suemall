package com.sue.pojo.vo;

/**
 * @author sue
 * @date 2020/8/1 16:28
 */

import lombok.Data;

import java.util.List;

/**
 * 最新商品VO
 */
@Data
public class NewItemsVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    private List<SimpleItemVO> simpleItemList;

}
