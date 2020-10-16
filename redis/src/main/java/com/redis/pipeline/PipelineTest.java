package com.redis.pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  redis通道test
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/03/20 11:45
 * @Version 1.0
 */
public class PipelineTest {

    public static void main(String[] args) {
        Jedis redis = new Jedis("127.0.0.1", 6379, 400000);
        /*Map<String, String> data = new HashMap<>();
        redis.select(6);
        redis.flushDB();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            data.put("k_" + i, "v_" + i);
            redis.hmset("key_" + i, data);
        }
        long end = System.currentTimeMillis();
        System.out.println("dbsize:[" + redis.dbSize() + "] .. ");
        System.out.println("hmset without pipeline used [" + (end - start) + "] seconds ..");


        redis.select(6);
        redis.flushDB();

        Pipeline p = redis.pipelined();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k_" + i, "v_" + i);
            p.hmset("key_" + i, data);
        }
        p.sync();//一次性提交所有命令
        end = System.currentTimeMillis();
        System.out.println("dbsize:[" + redis.dbSize() + "] .. ");
        System.out.println("hmset with pipeline used [" + (end - start) + "] seconds ..");*/


        // hmget
        redis.select(6);
        Set keys = redis.keys("*");
        // 直接使用Jedis hgetall
        long start = System.currentTimeMillis();
        Map<Object, Map<String, String>> result = new HashMap<>();
        for (Object key : keys) {
            result.put(key, redis.hgetAll(String.valueOf(key)));
        }
        long end = System.currentTimeMillis();
        System.out.println("result size:[" + result.size() + "] ..");
        System.out.println("hgetAll without pipeline used [" + (end-start) + "] seconds ..");

        // 使用pipeline hgetall
        Map<Object, Response<Map<String, String>>> responses = new HashMap<>(keys.size());
        result.clear();
        start = System.currentTimeMillis();
        Pipeline p = redis.pipelined();
        for (Object key : keys) {
            responses.put(key, p.hgetAll(String.valueOf(key)));//缓存命令，并将结果集放入到responses中，注意，这里并没有正在返回结果
        }
        p.sync();//提交所有命令
        for (Object k : responses.keySet()) {//一次性获取结果，因为pipeline是基于队列的所以顺序也能得到保障
            result.put(k, responses.get(k).get());
        }
        end = System.currentTimeMillis();
        System.out.println("result size:[" + result.size() + "] ..");
        System.out.println("hgetAll with pipeline used [" + (end-start) + "] seconds ..");
        redis.disconnect();
    }
}
