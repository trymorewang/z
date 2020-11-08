package com.rocketmq.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rocketmq.constant.RocketConstant;
import com.rocketmq.dao.OrderDao;
import com.rocketmq.dao.RepoDao;
import com.rocketmq.entity.OrderRecord;
import com.rocketmq.entity.WzRepo;
import com.rocketmq.producer.RepoTransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


/**
 * <p>
 *  订单监听事务消息
 *
 *  假设多个消息流传 其中一个步骤出现失败如何回滚
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 16:13
 * @Version 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "order-pay-group", topic = "Order-Success")
public class OrderConsumerListener implements RocketMQListener<OrderRecord> {

    @Autowired
    private RepoTransactionProducer repoTransactionProducer;

    @Override
    public void onMessage(OrderRecord orderRecord) {

        try {
            repoTransactionProducer.sendRepoSucessEvent(orderRecord.getOrderId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}
