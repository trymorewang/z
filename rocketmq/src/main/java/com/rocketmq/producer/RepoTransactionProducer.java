package com.rocketmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rocketmq.dao.OrderDao;
import com.rocketmq.dao.RepoDao;
import com.rocketmq.entity.*;
import com.rocketmq.service.OrderService;
import com.rocketmq.service.RepoService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RepoTransactionProducer implements InitializingBean {

    private TransactionMQProducer producer;

    @Autowired
    private RepoService repoService;

    @Autowired
    private RepoDao repoDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        producer = new TransactionMQProducer("repo-pay-group");
        producer.setNamesrvAddr("134.175.97.64:9876");
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("transaction-thread-name-%s").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(30), threadFactory);
        producer.setExecutorService(executor);
        //设置发送消息的回调
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 根据消息发送的结果 判断是否执行本地事务
             *
             * 回调该方法的时候说明 消息已经成功发送到了MQ，可以把订单状态更新为 "支付成功"
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 根据本地事务执行成与否判断 事务消息是否需要commit与 rollback
                ObjectMapper objectMapper = new ObjectMapper();
                LocalTransactionState state = LocalTransactionState.UNKNOW;
                try {
                    RepoRecord record = objectMapper.readValue(msg.getBody(), RepoRecord.class);

                    //MQ已经收到了TransactionProducer send方法发送的事务消息，下面执行本地的事务
                    repoService.updateRepoStatusByOrderId(record.getOrderId());
                    state = LocalTransactionState.COMMIT_MESSAGE;

                } catch (Exception e) {
                    e.printStackTrace();
                    state = LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return state;
            }
            /**
             * RocketMQ 回调 根据本地事务是否执行成功 告诉broker 此消息是否投递成功
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                ObjectMapper objectMapper = new ObjectMapper();
                LocalTransactionState state = LocalTransactionState.UNKNOW;
                RepoRecord record = null;
                try {
                    record = objectMapper.readValue(msg.getBody(), RepoRecord.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //根据是否有transaction_id对应记录 来判断事务是否执行成功
                    boolean isLocalSuccess = repoService.checkRepoSuccess(record.getOrderId());

                    if (isLocalSuccess) {
                        state = LocalTransactionState.COMMIT_MESSAGE;
                    } else {
                        state = LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return state;
            }
        });
        producer.start();
    }

    public void sendRepoSucessEvent(String orderId) throws JsonProcessingException, UnsupportedEncodingException, MQClientException, MQClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        WzRepo wzRepo = repoDao.findAll().stream()
                .filter(item->item.getOrderId().equals(orderId))
                .collect(Collectors.toList()).get(0);
        if(wzRepo == null){
            System.out.println("not found order " + orderId);
        }
        // 构造发送的事务 消息
        WarehouseRecord record = new WarehouseRecord();
        record.setOrderId(orderId);

        Message message = new Message("Repo-Success", "", record.getOrderId(),
                objectMapper.writeValueAsString(record).getBytes(RemotingHelper.DEFAULT_CHARSET));

        TransactionSendResult result = producer.sendMessageInTransaction(message, null);
        System.out.println("发送库存事务消息 ,orderId = " + record.getOrderId() + " " + result.toString());
    }
}
