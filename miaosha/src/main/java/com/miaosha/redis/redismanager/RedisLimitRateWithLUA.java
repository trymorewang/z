package com.miaosha.redis.redismanager;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 *  redis lua 通过ip分布式限流
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/23 16:13
 * @Version 1.0
 */
@Slf4j
public class RedisLimitRateWithLUA {


    public static boolean accquire() {
        Jedis jedis = null;
        try {
            jedis = RedisManager.getJedis();
        } catch (Exception e) {
            log.error("获取jedis失败：{}", e.getMessage());
            return false;
        }

        String lua =
                "local key = KEYS[1] " +
                        " local limit = tonumber(ARGV[1]) " +
                        " local current = tonumber(redis.call('get', key) or '0')" +
                        " if current + 1 > limit " +
                        " then  return 0 " +
                        " else " +
                        " redis.call('INCRBY', key,'1')" +
                        " redis.call('expire', key,'2') " +
                        " end return 1 ";

        // 当前秒
        String key = "ip:" + System.currentTimeMillis() / 1000;
        // 最大限制
        String limit = "3";
        List<String> keys = new ArrayList<String>();
        keys.add(key);
        List<String> args = new ArrayList<String>();
        args.add(limit);
        //jedis.auth("password");
        String luaScript = jedis.scriptLoad(lua);
        Long result = (Long) jedis.evalsha(luaScript, keys, args);
        return result == 1;
    }

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    System.out.println("请求是否被执行：" + accquire());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        latch.countDown();
    }
}
