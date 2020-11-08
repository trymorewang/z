package com.rocketmq.consume;

import com.rocketmq.entity.RepoRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "repo-pay-group", topic = "Repo-Success")
public class WarehouseConsumerListener implements RocketMQListener<RepoRecord> {
    @Override
    public void onMessage(RepoRecord repoRecord) {

    }
}
