package com.miaosha.controller;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaUser;
import com.miaosha.common.entity.OrderInfo;
import com.miaosha.common.enums.resultbean.ResultGeekQ;
import com.miaosha.common.vo.OrderDetailVo;
import com.miaosha.redis.RedisService;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoShaUserService;
import com.miaosha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.miaosha.common.enums.enums.ResultStatus.ORDER_NOT_EXIST;
import static com.miaosha.common.enums.enums.ResultStatus.SESSION_ERROR;


@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoShaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public ResultGeekQ<OrderDetailVo> info(Model model, MiaoshaUser user,
										   @RequestParam("orderId") long orderId) {
		ResultGeekQ<OrderDetailVo> result = ResultGeekQ.build();
		if (user == null) {
			result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
			return result;
		}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
			result.withError(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
			return result;
    	}
    	long goodsId = order.getGoodsId();
		GoodsVoOrder goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	result.setData(vo);
    	return result;
    }
    
}
