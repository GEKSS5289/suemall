package com.sue.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Table(name = "items_img", schema = "foodie-shop-dev", catalog = "")
@Data
public class ItemsImg {
    @Id
    private String id;
    private String itemId;
    private String url;
    private int sort;
    private int isMain;
    private Timestamp createdTime;
    private Timestamp updatedTime;

}
