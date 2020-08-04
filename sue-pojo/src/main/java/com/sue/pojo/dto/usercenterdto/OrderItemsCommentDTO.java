package com.sue.pojo.dto.usercenterdto;

import lombok.Data;

@Data
public class OrderItemsCommentDTO {
	private String commentId;
	private String itemId;
	private String itemName;
	private String itemSpecId;
	private String itemSpecName;
	private Integer commentLevel;
	private String content;
}
