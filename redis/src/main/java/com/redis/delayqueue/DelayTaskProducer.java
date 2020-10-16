package com.redis.delayqueue;

import redis.clients.jedis.Jedis;


/**
 * <p>
 * 生产者
 * </P>
 *
 * @author wangzhi
 */
public class DelayTaskProducer {

    public void produce(String newsId, long timeStamp) {
        try(Jedis client = RedisClient.getClient()) {
            client.zadd(Constants.DELAY_TASK_QUEUE, timeStamp, newsId);
        }
    }
}
