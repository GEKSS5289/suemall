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
@Table(name = "items_param", schema = "foodie-shop-dev", catalog = "")
@Data
public class ItemsParam {
    @Id
    private String id;
    private String itemId;
    private String producPlace;
    private String footPeriod;
    private String brand;
    private String factoryName;
    private String factoryAddress;
    private String packagingMethod;
    private String weight;
    private String storageMethod;
    private String eatMethod;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
