package com.miaosha2.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MiaoshaGoods {
	private Long id;
	private Long goodsId;
	private String goodsName;
	private Double miaoshaPrice;
	private Integer stockCount;
	private Long version;
	private Date startDate;
	private Date endDate;
}
