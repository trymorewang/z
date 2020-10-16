package com.rocketmq.message.order;

import com.rocketmq.constant.RocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


/**
 * <p>
 *  有序消费  consumeMode = ConsumeMode.ORDERLY
 *  仅限于一个分区的情况下，多个分区会乱序
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 16:13
 * @Version 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = RocketConstant.ConsumerGroup.Z_ORDER_CONSUMER, topic = RocketConstant.Topic.Z_ORDER_TOPIC, consumeMode = ConsumeMode.ORDERLY)
public class StringOrderConsumerListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String messageExt) {
        log.info("receive message: {}", messageExt);
    }
}
