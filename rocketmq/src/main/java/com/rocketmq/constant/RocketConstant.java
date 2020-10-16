package com.rocketmq.constant;


public interface RocketConstant {
    interface ConsumerGroup {

        String SPRING_BOOT_CONSUMER = "spring-boot-consumer";

        String Z_CONSUMER = "z-consumer";
        String Z_ORDER_CONSUMER = "z_order_consumer";
        String Z_USER_CONSUMER = "z_user_consumer";
    }

    interface Topic {

        String SPRING_BOOT_TOPIC = "spring-boot-topic";

        String Z_TOPIC = "z-topic";
        String Z_ORDER_TOPIC = "z_order_topic";
        String Z_USER_TOPIC = "z_user_topic";
    }

    interface HashKey {
        String ORDER_KEY = "order_key";
    }
}
