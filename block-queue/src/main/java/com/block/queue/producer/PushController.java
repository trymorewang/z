package com.block.queue.producer;

import com.alibaba.fastjson.JSONObject;
import com.block.queue.core.ZQueue;
import com.block.queue.core.ZQueue1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 16:49
 * @Version 1.0
 */
@Slf4j
@RestController
public class PushController {

    @Autowired
    private ZQueue1 zQueue;

    @GetMapping("/push")
    public String test() throws InterruptedException {
        /*for (int i = 0; i < 10; i++) {
            String tag = i % 2 == 0 ? "topic1" : "topic2";
            product(tag);
        }*/

        for (int i = 0; i < 10; i++) {
            JSONObject message = new JSONObject();
            //String tag = i % 2 == 0 ? "topic1" : "topic2";
            String tag = "topic1";
            message.put("tag", tag);
            message.put("body", "test");
            zQueue.push(message);
        }
        return "{ \"status\": \"success\" }";

    }

    private void product(String topic) throws InterruptedException {
        JSONObject message = new JSONObject();
        message.put("tag", topic);
        message.put("body", "test");
        zQueue.push(message);
    }
}
