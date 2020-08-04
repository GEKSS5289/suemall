package com.sue.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class MySubOrderItemVO {
	private String itemId;
	private String itemImg;
	private String itemName;
	private String itemSpecName;
	private Integer buyCounts;
	private Integer price;

}
