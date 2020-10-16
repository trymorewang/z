package com.miaosha2.core;

import com.miaosha2.service.RedisService;
import com.miaosha2.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.*;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/30 16:36
 * @Version 1.0
 */
@Slf4j
public class RedisListener implements InitializingBean {


    private List<RedisHandler> handlers;
    private Executor product;
    private Executor consumer;

    @Autowired
    private RedisService redisService;


    public RedisListener() {
    }


    /**
     * 队列监听
     *
     * brpop超时参数不可设置为0，容易出现一系列问题，比如部分数据丢失
     * 关于这个问题的issues https://github.com/lettuce-io/lettuce-core/issues/757
     */
    public void redisTask(RedisHandler redisHandler) {
        while (true) {
            try {
                String msgBody = redisService.brpop(1, redisHandler.queueName());
                consumer.execute(() -> {
                    redisHandler.consume(msgBody);
                });
            } catch (Exception e) {
                //log.info("队列为空，暂时让出cpu时间片");

                //Thread.yield();
            }
        }
    }

    @Autowired
    private void setProduct(Executor product) {
        this.product = product;
    }

    @Autowired
    private void setConsumer(Executor consumer) {
        this.consumer = consumer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlers = SpringUtil.getBeans(RedisHandler.class);
        /*product = new ThreadPoolExecutor(10, 15, 60 * 3,
                TimeUnit.SECONDS, new SynchronousQueue<>());
        consumer = new ThreadPoolExecutor(10, 15, 60 * 3,
                TimeUnit.SECONDS, new SynchronousQueue<>());*/
        for (RedisHandler redisHandler : handlers) {
            product.execute(() -> {
                redisTask(redisHandler);
            });
        }
    }
}
