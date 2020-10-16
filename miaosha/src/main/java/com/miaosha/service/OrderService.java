package com.miaosha.service;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaOrder;
import com.miaosha.common.entity.MiaoshaUser;
import com.miaosha.common.entity.OrderInfo;
import com.miaosha.common.utils.DateTimeUtils;
import com.miaosha.mapper.OrderMapper;
import com.miaosha.redis.OrderKey;
import com.miaosha.redis.RedisService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.miaosha.common.enums.Constanst.orderStaus.ORDER_NOT_PAY;


@Service
public class OrderService {
	
	@Autowired
	OrderMapper orderMapper;

	@Autowired
	private RedisService redisService ;
	
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
		return	redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,MiaoshaOrder.class) ;
	}

	public OrderInfo getOrderById(long orderId) {
		return orderMapper.getOrderById(orderId);
	}

	@Transactional
	public OrderInfo createOrder(MiaoshaUser user, GoodsVoOrder goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(Long.valueOf(user.getNickname()));
		orderMapper.insert(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(Long.valueOf(user.getNickname()));
		orderMapper.insertMiaoshaOrder(miaoshaOrder);
		redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getNickname()+"_"+goods.getId(),miaoshaOrder) ;
		return orderInfo;
	}

	public void closeOrder(int hour){
		Date closeDateTime = DateUtils.addHours(new Date(),-hour);
		List<OrderInfo> orderInfoList = orderMapper.selectOrderStatusByCreateTime(Integer.valueOf(ORDER_NOT_PAY.ordinal()), DateTimeUtils.dateToStr(closeDateTime));
		for (OrderInfo orderInfo:orderInfoList){
			System.out.println("orderinfo  infomation "+orderInfo.getGoodsName());
		}
	}

	
}
