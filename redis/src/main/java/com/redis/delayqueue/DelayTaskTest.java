package com.redis.delayqueue;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class DelayTaskTest {

    public static void main(String[] args) {
        DelayTaskProducer producer = new DelayTaskProducer();
        long now = System.currentTimeMillis();
        System.out.println(MessageFormat.format("start time - {0}", now));
        producer.produce("1", now + TimeUnit.SECONDS.toMillis(5));
        producer.produce("2", now + TimeUnit.SECONDS.toMillis(10));
        producer.produce("3", now + TimeUnit.SECONDS.toMillis(15));
        producer.produce("4", now + TimeUnit.SECONDS.toMillis(20));


        //模式10个消费者实例
        for (int i = 0; i < 10; i++) {
            new DelayTaskConsumer().start();
        }
    }

}
