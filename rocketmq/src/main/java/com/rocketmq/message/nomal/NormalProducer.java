package com.rocketmq.message.nomal;

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
@RequestMapping("/normal")
public class NormalProducer {

    @Resource
    private RocketMQTemplate mqTemplate;

    /**
     * <p>
     * 单条发送
     * </p>
     *
     * @param msg
     * @return
     */

    @GetMapping("/send")
    public String send() {
        for (int i = 0; i < 10; i++) {
            mqTemplate.send(RocketConstant.Topic.Z_TOPIC, MessageBuilder.withPayload("创建" + i).build());
            mqTemplate.send(RocketConstant.Topic.Z_TOPIC, MessageBuilder.withPayload("支付" + i).build());
            mqTemplate.send(RocketConstant.Topic.Z_TOPIC, MessageBuilder.withPayload("完成" + i).build());
        }
        return "success";
    }
    /*@GetMapping("/send")
    public String send(@RequestParam String msg) {
        for (int i = 0; i < 30; i++) {
            mqTemplate.send(RocketConstant.Topic.Z_TOPIC, MessageBuilder.withPayload(msg + "-" + i).build());
        }
        return "success";
    }*/

    /**
     * <p>
     * 批量发送
     * </p>
     *
     * @param msg
     * @param time
     * @return
     */
    @GetMapping("/sendBatch")
    public String sendBatch(@RequestParam String msg, @RequestParam Integer time) {
        for (int i = 0; i < time; i++) {
            mqTemplate.send(RocketConstant.Topic.Z_TOPIC, MessageBuilder.withPayload(msg + "-" + i).build());
        }
        return "success";
    }

    /**
     * <p>
     * 发送对象类型消息
     * </p>
     *
     * @return
     */
    @GetMapping("/sendUser")
    public String sendUser() {
        for (int i = 0; i < 30; i++) {
            UserInfo userInfo = UserInfo.builder().id(1).name("liuxin" + i).build();
            mqTemplate.send(RocketConstant.Topic.Z_USER_TOPIC, MessageBuilder.withPayload(userInfo).build());
        }
        return "success";
    }
}
