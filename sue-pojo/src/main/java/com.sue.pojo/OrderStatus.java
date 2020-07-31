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
@Table(name = "order_status", schema = "foodie-shop-dev", catalog = "")
@Data
public class OrderStatus {
    @Id
    private String orderId;
    private int orderStatus;
    private Timestamp createdTime;
    private Timestamp payTime;
    private Timestamp deliverTime;
    private Timestamp successTime;
    private Timestamp closeTime;
    private Timestamp commentTime;

}
