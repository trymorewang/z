package com.redis.delayqueue;

import redis.clients.jedis.Jedis;

public class RedisClient {

    public static Jedis getClient() {
        return new Jedis("127.0.0.1", 6379);
    }
}
