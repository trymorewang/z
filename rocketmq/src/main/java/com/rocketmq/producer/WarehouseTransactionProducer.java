package com.rocketmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rocketmq.dao.OrderDao;
import com.rocketmq.dao.WarehouseDao;
import com.rocketmq.entity.OrderRecord;
import com.rocketmq.entity.WarehouseRecord;
import com.rocketmq.entity.WzOrder;
import com.rocketmq.entity.WzWarehouse;
import com.rocketmq.service.OrderService;
import com.rocketmq.service.WarehouseService;
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
public class WarehouseTransactionProducer implements InitializingBean {

    private TransactionMQProducer producer;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseDao warehouseDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        producer = new TransactionMQProducer("warehouse-group");
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
             * 回调该方法的时候说明 消息已经成功发送到了MQ，可以把仓储状态更新为 "发货成功"
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 根据本地事务执行成与否判断 事务消息是否需要commit与 rollback
                ObjectMapper objectMapper = new ObjectMapper();
                LocalTransactionState state = LocalTransactionState.UNKNOW;
                try {
                    WarehouseRecord record = objectMapper.readValue(msg.getBody(), WarehouseRecord.class);

                    //根据是否有transaction_id对应转账记录 来判断事务是否执行成功
                    warehouseService.updateDeliveryStatusByOrderId(record.getOrderId());
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
                WarehouseRecord record = null;
                try {
                    record = objectMapper.readValue(msg.getBody(), WarehouseRecord.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //根据是否有transaction_id对应记录 来判断事务是否执行成功
                    boolean isLocalSuccess = warehouseService.checkWarehouseSuccess(record.getOrderId());

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

    public void sendWarehouseSucessEvent(String orderId) throws JsonProcessingException, UnsupportedEncodingException, MQClientException, MQClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        WzWarehouse wzWarehouse = warehouseDao.findAll().stream()
                .filter(item->item.getOrderId().equals(orderId))
                .collect(Collectors.toList()).get(0);
        if(wzWarehouse == null){
            System.out.println("not found order " + orderId);
        }
        // 构造发送的事务 消息
        WarehouseRecord record = new WarehouseRecord();
        record.setDeliveryStatus(wzWarehouse.getDeliveryStatus());
        record.setLogisticsId(wzWarehouse.getLogisticsId());
        record.setDeliveryStatus(wzWarehouse.getDeliveryStatus());

        Message message = new Message("Warehouse-Success", "", record.getOrderId(),
                objectMapper.writeValueAsString(record).getBytes(RemotingHelper.DEFAULT_CHARSET));

        TransactionSendResult result = producer.sendMessageInTransaction(message, null);
        System.out.println("发送仓储事务消息 ,orderId = " + record.getOrderId() + " " + result.toString());
    }
}
