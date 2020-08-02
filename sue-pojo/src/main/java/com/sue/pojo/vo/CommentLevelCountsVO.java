package com.sue.pojo.vo;

/**
 * @author sue
 * @date 2020/8/2 8:24
 */

import lombok.Data;

/**
 * 用于展示商品评价数量VO
 */
@Data
public class CommentLevelCountsVO {
    public Integer totalCounts;
    public Integer goodCounts;
    public Integer normalCounts;
    public Integer badCounts;
}
