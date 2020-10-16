package com.rocketmq.message.nomal;

import com.rocketmq.constant.RocketConstant;
import com.rocketmq.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/05/09 16:46
 * @Version 1.0
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = RocketConstant.ConsumerGroup.Z_USER_CONSUMER, topic = RocketConstant.Topic.Z_USER_TOPIC)
@Component
public class UserRocketMQListener implements RocketMQListener<UserInfo> {

    @Override
    public void onMessage(UserInfo userInfo) {
        log.info("receive user: {}", userInfo);
    }
}
