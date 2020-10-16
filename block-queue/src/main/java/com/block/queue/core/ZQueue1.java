package com.block.queue.core;

import com.alibaba.fastjson.JSONObject;
import com.block.queue.consumer.DeadTopicConsumer;
import com.block.queue.multimap.ZLinkedMultiValueMap;
import com.block.queue.multimap.ZMultiValueMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * <p>
 *  SmartInitializingSingleton: 此接口将初始化方法的调用和bean的创建过程分开了
 *
 *  DefaultListableBeanFactory#preInstantiateSingletons
 *  所有bean初始化后，遍历判断每个singleton bean是都实现了SmartInitializingSingleton，然后对此接口的实例调用afterSingletonsInstantiated()
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/16 15:28
 * @Version 1.0
 */
@Slf4j
@Component
public class ZQueue1 implements Runnable, DisposableBean, ApplicationContextAware, SmartInitializingSingleton {

    private ConfigurableApplicationContext applicationContext;

    private StandardEnvironment environment;

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

    public ZQueue1(StandardEnvironment environment) {

        this.environment = environment;

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
        if (Objects.nonNull(callBack)) {
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
    public void afterSingletonsInstantiated() {
        List<Object> beans = this.applicationContext.getBeansWithAnnotation(BlockQueueConsumer.class)
                .entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());

        beans.forEach(this::registerContainer);
    }

    private void registerContainer(Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        BlockQueueConsumer annotation = clazz.getAnnotation(BlockQueueConsumer.class);
        String topic = this.environment.resolvePlaceholders(annotation.topic());
        //判断bean是否继承了消费者接口
        if (QueueListener.class.isAssignableFrom(bean.getClass())) {
            //if (bean instanceof QueueListener) {
            if (StringUtils.isNotBlank(topic)) {
                CONSUME_MAP.add(topic, (QueueListener<JSONObject>) bean);
            } else {
                CONSUME_MAP.add(DEAD_TOPIC, deadTopicConsumer);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
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
