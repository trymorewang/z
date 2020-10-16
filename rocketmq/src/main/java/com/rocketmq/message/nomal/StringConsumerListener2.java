package com.rocketmq.message.nomal;

import com.rocketmq.constant.RocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
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
//@Component
@RocketMQMessageListener(consumerGroup = RocketConstant.ConsumerGroup.Z_CONSUMER, topic = RocketConstant.Topic.Z_TOPIC)
public class StringConsumerListener2 implements RocketMQListener<String> {

    @Override
    public void onMessage(String messageExt) {
        log.info("consume2 - receive message: {}", messageExt);
    }
}
