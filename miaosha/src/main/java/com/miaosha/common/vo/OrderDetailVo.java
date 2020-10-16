package com.miaosha.common.vo;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.OrderInfo;
import lombok.*;

@Data
public class OrderDetailVo {
	private GoodsVoOrder goods;
	private OrderInfo order;

}
