package com.rocketmq.message.trans;

import com.rocketmq.constant.RocketConstant;
import com.rocketmq.entity.UserInfo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/05/09 16:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/trans")
public class TransProducer {


    @GetMapping("/send")
    public String send() {
        ProducerACL producerACL = new ProducerACL();
        producerACL.testTransaction();
        return "success";
    }

}
