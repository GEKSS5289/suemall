package com.sue.pojo;

import lombok.Data;

@Data
public class Category {
    private Integer id;

    private String name;

    private Integer type;

    private Integer fatherId;

    private String logo;

    private String slogan;

    private String catImage;

    private String bgColor;


}