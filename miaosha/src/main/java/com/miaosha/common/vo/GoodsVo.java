package com.miaosha.common.vo;

import com.miaosha.common.entity.Goods;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
public class GoodsVo extends Goods {
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
}
