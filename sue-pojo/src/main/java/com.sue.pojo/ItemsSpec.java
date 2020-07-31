package com.sue.pojo;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Table(name = "items_spec", schema = "foodie-shop-dev", catalog = "")
@Data
public class ItemsSpec {
    @Id
    private String id;
    private String itemId;
    private String name;
    private int stock;
    private BigDecimal discounts;
    private int priceDiscount;
    private int priceNormal;
    private Timestamp createdTime;
    private Timestamp updatedTime;

}
