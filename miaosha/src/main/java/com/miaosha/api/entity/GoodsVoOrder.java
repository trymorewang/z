package com.miaosha.api.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
public class GoodsVoOrder implements Serializable {
	private Long id;
	private String goodsName;
	private String goodsTitle;
	private String goodsImg;
	private String goodsDetail;
	private Double goodsPrice;
	private Integer goodsStock;
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
}
