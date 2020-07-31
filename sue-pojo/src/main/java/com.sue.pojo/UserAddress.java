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
@Table(name = "user_address", schema = "foodie-shop-dev")
@Data
public class UserAddress {
    @Id
    private String id;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String extand;
    private Integer isDefault;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
