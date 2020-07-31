package com.sue.pojo;

import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;


/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Data
public class Carousel {
    @Id
    private String id;
    private String imageUrl;
    private String backgroundColor;
    private String itemId;
    private String catId;
    private int type;
    private int sort;
    private int isShow;
    private Timestamp createTime;
    private Timestamp updateTime;


}
