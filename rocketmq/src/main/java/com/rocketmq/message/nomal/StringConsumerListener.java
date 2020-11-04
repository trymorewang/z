package com.rocketmq.message.nomal;

import com.alibaba.fastjson.JSONObject;
//import com.block.queue.core.ZQueue;
import com.rocketmq.constant.RocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * <p>
 *  消费的时候默认是多线程消费的无法保证顺序性
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 16:13
 * @Version 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = RocketConstant.ConsumerGroup.Z_CONSUMER, topic = RocketConstant.Topic.Z_TOPIC)
public class StringConsumerListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {

    }

    /*@Autowired
    private ZQueue zQueue;

    @Override
    public void onMessage(String s) {
        //log.info("consume1 - receive message: {}", messageExt);
        JSONObject message = new JSONObject();
        String tag = "topic1";
        message.put("tag", tag);
        message.put("body", s);
        zQueue.push(message);
    }*/
}
