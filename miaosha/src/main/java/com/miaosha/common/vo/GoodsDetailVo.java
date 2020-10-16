package com.miaosha.common.vo;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaUser;
import lombok.*;


@Data
public class GoodsDetailVo {
	private int miaoshaStatus = 0;
	private int remainSeconds = 0;
	private GoodsVoOrder goods ;
	private MiaoshaUser user;

}
