package com.sue.pojo;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author sue
 * @date 2020/7/31 13:29
 */

@Entity
@Data
public class Category {
    @Id
    private int id;
    private String name;
    private int type;
    private int fatherId;
    private String logo;
    private String slogan;
    private String catImage;
    private String bgColor;

}
