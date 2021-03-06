package com.block.queue.consumer;

import com.alibaba.fastjson.JSONObject;
import com.block.queue.core.BlockQueueConsumer;
import com.block.queue.core.QueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  死信队列
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 16:13
 * @Version 1.0
 */
@Slf4j
@Component
@BlockQueueConsumer
public class DeadTopicConsumer implements QueueListener<JSONObject> {
    @Override
    public void execute(JSONObject var1) {
        log.info("死信消费 - " + Thread.currentThread().getName() + "- get mssage:" + var1);
    }
}
