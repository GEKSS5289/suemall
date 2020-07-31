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
@Table(name = "items_comments", schema = "foodie-shop-dev", catalog = "")
@Data
public class ItemsComments {
    @Id
    private String id;
    private String userId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String sepcName;
    private int commentLevel;
    private String content;
    private Timestamp createdTime;
    private Timestamp updatedTime;

}
