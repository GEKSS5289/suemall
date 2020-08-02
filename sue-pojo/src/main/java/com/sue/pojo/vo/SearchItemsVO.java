package com.sue.pojo.vo;

/**
 * @author sue
 * @date 2020/8/2 9:44
 */

import lombok.Data;

/**
 * 用于展示商品搜索结果的VO
 *
 */
@Data
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private int sellCounts;
    private String imgUrl;
    private int price;

}
