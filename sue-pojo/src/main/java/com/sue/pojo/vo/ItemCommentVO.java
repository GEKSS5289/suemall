package com.sue.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author sue
 * @date 2020/8/2 8:48
 */

/**
 * 这是用于展示商品评价的VO
 */
@Data
public class ItemCommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
