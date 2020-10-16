package com.block.queue.core;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.block.queue.consumer.DeadTopicConsumer;
import com.block.queue.multimap.ZLinkedMultiValueMap;
import com.block.queue.multimap.ZMultiValueMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * 1、当ApplicationContext被初始化或刷新时，会触发ContextRefreshedEvent事件然后执行onApplicationEvent()方法
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 15:28
 * @Version 1.0
 */
@Slf4j
@Component
public class ZQueue implements Runnable, DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    /**
     * 将要消费的所有主题消息都将投入此队列
     */
    private static final BlockingQueue<JSONObject> BLOCKING_QUEUE = new LinkedBlockingQueue();
    /**
     * 一个topic对应一个或消费者模式（多个可拓展）
     */
    private static final ZMultiValueMap<String, QueueListener<JSONObject>> CONSUME_MAP = new ZLinkedMultiValueMap<>();

    private static final String DEAD_TOPIC = "deadtopic";
    private static final String TAG = "tag";

    Executor asyncServiceExecutor;
    DeadTopicConsumer deadTopicConsumer;

    public ZQueue() {
        Thread thread = new Thread(this);
        thread.setName("ZQueue Lister Thread");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * send msg
     *
     * @param o
     */
    public void push(JSONObject o) {
        try {
            BLOCKING_QUEUE.put(o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * consume msg
     */
    public void consume(JSONObject o) {
        String topic = o.get(TAG).toString();
        QueueListener<JSONObject> callBack = CONSUME_MAP.poll(topic);
        if (ObjectUtil.isNotNull(callBack)) {
            asyncServiceExecutor.execute(() -> callBack.execute(o));
        } else {
            asyncServiceExecutor.execute(() -> deadTopicConsumer.execute(o));
        }
    }


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                consume(BLOCKING_QUEUE.take());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        log.info("容器初始化后......");

        //避免执行多次（有多个容器时会执行多次）
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            Map<String, Object> beanMap = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(BlockQueueConsumer.class);
            for (Object o : beanMap.values()) {
                String topic = AnnotationUtil.getAnnotationValue(o.getClass(), BlockQueueConsumer.class, "topic");
                //判断bean是否继承了消费者接口
                if (o instanceof QueueListener) {
                    if (StringUtils.isNotBlank(topic)) {
                        CONSUME_MAP.add(topic, (QueueListener<JSONObject>) o);
                    } else {
                        CONSUME_MAP.add(DEAD_TOPIC, deadTopicConsumer);
                    }
                }
            }
        }
    }

    @Autowired
    public void setDeadTopicConsumer(DeadTopicConsumer deadTopicConsumer) {
        this.deadTopicConsumer = deadTopicConsumer;
    }

    @Autowired
    public void setAsyncServiceExecutor(Executor asyncServiceExecutor) {
        this.asyncServiceExecutor = asyncServiceExecutor;
    }

    @Override
    public void destroy() throws Exception {

    }
}
