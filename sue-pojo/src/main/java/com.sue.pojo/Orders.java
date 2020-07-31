package com.sue.pojo;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Data
public class Orders {
    @Id
    private String id;
    private String userId;
    private String receiverName;
    private String receiverMobile;
    private String receiverAddress;
    private int totalAmount;
    private int realPayAmount;
    private int postAmount;
    private int payMethod;
    private String leftMsg;
    private String extand;
    private int isComment;
    private int isDelete;
    private Timestamp createdTime;
    private Timestamp updatedTime;

}
