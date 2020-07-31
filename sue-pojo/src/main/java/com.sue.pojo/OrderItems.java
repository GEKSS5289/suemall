package com.sue.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Table(name = "order_items", schema = "foodie-shop-dev", catalog = "")
@Data
public class OrderItems {
    @Id
    private String id;
    private String orderId;
    private String itemId;
    private String itemImg;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private int price;
    private int buyCounts;
}
