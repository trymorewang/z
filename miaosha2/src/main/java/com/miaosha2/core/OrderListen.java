package com.miaosha2.core;

import com.alibaba.fastjson.JSONObject;
import com.miaosha2.constant.PlatformConstants;
import com.miaosha2.dao.OrderMapper;
import com.miaosha2.entity.Message;
import com.miaosha2.entity.MiaoshaOrder;
import com.miaosha2.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/30 16:34
 * @Version 1.0
 */
@Slf4j
@Component
public class OrderListen implements RedisHandler {

    private final RedisService redisService;

    private final OrderMapper orderMapper;

    public OrderListen(RedisService redisService, OrderMapper orderMapper) {
        this.redisService = redisService;
        this.orderMapper = orderMapper;
    }


    @Override
    public String queueName() {
        return PlatformConstants.ORDER_QUEUE_XXX;
    }

    @Override
    public String consume(String msgBody) {
        //log.info("get message == >>" + msgBody);
        MiaoshaOrder miaoshaOrder = JSONObject.parseObject(msgBody, MiaoshaOrder.class);
        String key = "miaosha_" + miaoshaOrder.getGoodsId();
        log.info(miaoshaOrder + "：下单成功，订单生成待支付状态");

        //进入延时队列，默认10分钟不支付则取消订单(测试是10秒)
        String timeQueue = key + "_time_queue";
        //实际使用TimeUnit.SECONDS.convert(10, TimeUnit.MINUTES) 10分钟转换为秒数
        //TimeUnit.SECONDS.toMillis(1)     1秒转换为毫秒数


        //订单记录数据库
        orderMapper.insert(miaoshaOrder);

        //进入超时队列(支付不支付都进入超时队列，秒杀场景实际成功的订单量少)
        redisService.zadd(timeQueue, (double) System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10), msgBody);
        return null;
    }
}
