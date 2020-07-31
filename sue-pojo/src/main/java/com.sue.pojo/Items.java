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
public class Items {
    @Id
    private String id;
    private String itemName;
    private int catId;
    private int rootCatId;
    private int sellCounts;
    private int onOffStatus;
    private String content;
    private Timestamp createdTime;
    private Timestamp updatedTime;


}
