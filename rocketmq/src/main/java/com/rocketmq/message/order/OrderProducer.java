package com.rocketmq.message.order;

import com.rocketmq.constant.RocketConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  顺序消息生产者
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/05/09 16:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderProducer {

    @Resource
    private RocketMQTemplate mqTemplate;

    @GetMapping("/send")
    public String send() {
        for (int i = 0; i < 10; i++) {
            mqTemplate.sendOneWayOrderly(RocketConstant.Topic.Z_ORDER_TOPIC, MessageBuilder.withPayload("创建" + i).build(), RocketConstant.HashKey.ORDER_KEY);
            mqTemplate.sendOneWayOrderly(RocketConstant.Topic.Z_ORDER_TOPIC, MessageBuilder.withPayload("支付" + i).build(), RocketConstant.HashKey.ORDER_KEY);
            mqTemplate.sendOneWayOrderly(RocketConstant.Topic.Z_ORDER_TOPIC, MessageBuilder.withPayload("完成" + i).build(), RocketConstant.HashKey.ORDER_KEY);
        }
        return "success";
    }

    /*@GetMapping("/send")
    public String send(@RequestParam String msg) {
        for (int i = 0; i < 30; i++) {
            mqTemplate.sendOneWayOrderly(RocketConstant.Topic.Z_ORDER_TOPIC, MessageBuilder.withPayload(msg + i).build(), RocketConstant.HashKey.ORDER_KEY);
        }
        return "success";
    }*/
}
