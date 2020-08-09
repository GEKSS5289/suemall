package com.sue.pojo.vo;


import com.sue.pojo.dto.malldto.ShopcartDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {
	private String orderId;
	private MerchantOrdersVO merchantOrdersVO;
	private List<ShopcartDTO> toBeRemovedShopcatdList;
}
