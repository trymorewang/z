# RocketMQ使用指南

1、继承RocketMQPushConsumerLifecycleListener可以重写prepareStart设置拉取时间间隔和每次拉取消息数量

```tex
@Override
public void prepareStart(DefaultMQPushConsumer consumer) {
    // 每次拉取的间隔，单位为毫秒
    consumer.setPullInterval(2000);
    // 单次pull消息的最大条数为broker的maxTransferCountOnMessageInMemory=32，即pullBatchSize>32将不起效，除非重新设置maxTransferCountOnMessageInMemory
    // 设置每次拉取的消息数为32
    consumer.setPullBatchSize(16);
}
```



---



2、在@RocketMQMessageListener(nameServer = "127.0.0.1:9877", instanceName = "tradeCluster") 代表使用某个特定的MQ集群

消息消费默认是异步多线程（如下日志）

```tex
2020-09-08 11:29:26.629  INFO 12700 --- [MessageThread_1] c.s.x.r.o.StringOrderConsumerListener    : receive message: 西天求精0
2020-09-08 11:29:26.629  INFO 12700 --- [MessageThread_1] a.r.s.s.DefaultRocketMQListenerContainer : consume C0A80189319C00B4AAC22683B8040000 cost: 1 ms
2020-09-08 11:29:26.657  INFO 12700 --- [MessageThread_2] c.s.x.r.o.StringOrderConsumerListener    : receive message: 西天求精1
2020-09-08 11:29:26.657  INFO 12700 --- [MessageThread_2] a.r.s.s.DefaultRocketMQListenerContainer : consume C0A80189319C00B4AAC22683B8490002 cost: 0 ms


```

