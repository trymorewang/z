package com.miaosha2.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaosha2.functional.SyncCommandCallback;
import com.miaosha2.service.RedisService;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * redis接口实现
 * <p>
 * Spring Boot高版本默认使用Lettuce作为Redis客户端（），同步使用时，应通过连接池提高效率。
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/25 13:01
 * @Version 1.0
 */
@Slf4j
@Service
public class RedisServerImpl implements RedisService {

    @Autowired
    RedisClient redisClient;

    @Autowired
    ObjectMapper objectMapper;

    GenericObjectPool<StatefulRedisConnection<String, String>> redisConnectionPool;


    @Override
    public <T> T set(String var1, T var2) {
        return (T) executeSync(commands -> commands.set(var1, writeValueAsString(var2)));
    }

    @Override
    public <T> T setex(String var1, long var2, T var3) {
        return (T) executeSync(commands -> commands.setex(var1, var2, writeValueAsString(var3)));
    }

    @Override
    public <T> T setnx(String var1, String var2) {
        return (T) executeSync((commands -> commands.setnx(var1, var2)));
    }

    @Override
    public <T> T get(String var1, Class<T> clazz) {
        return executeSync(commands -> readValue(commands.get(var1), clazz));
    }


    @Override
    public <T> T scriptLoad(String var1) {
        return (T) executeSync(commands -> commands.scriptLoad(var1));
    }

    @Override
    public <T> T evalsha(String var1, ScriptOutputType var2, String[] var3, String[] var4) {
        return executeSync(commands -> commands.evalsha(var1, var2, var3, var4));
    }

    @Override
    public Long lPush(String var1, Object... var2) {
        return executeSync(commands -> commands.lpush(var1, varargsConvert(var2)));
    }


    @Override
    public Long llen(String var1) {
        return executeSync(commands -> commands.llen(var1));
    }

    @Override
    public <T> T brpop(long var1, String var2) {
        return (T) executeSync(commands -> commands.brpop(var1, var2).getValue());
    }

    @Override
    public Long incr(String var1) {
        return executeSync(commands -> commands.incr(var1));
    }

    @Override
    public Boolean exists(String... ks) {
        return executeSync(commands -> commands.exists(ks)).intValue() == ks.length;
    }

    @Override
    public Boolean hset(String var1, String var2, Object var3) {
        return executeSync(commands -> commands.hset(var1, var2, writeValueAsString(var3)));
    }

    @Override
    public Long hset(String var1, Map<String, String> var2) {
        return executeSync(commands -> commands.hset(var1, var2));
    }

    @Override
    public <T> T hget(String var1, String var2) {
        return (T) executeSync(commands -> commands.hget(var1, var2));
    }

    @Override
    public <T> T zadd(String var1, Double var2, T var3) {
        return (T) executeSync(commands -> commands.zadd(var1, var2, writeValueAsString(var3)));
    }

    @Override
    public <T> T zrangebyscore(String var1, Range<Number> var2, Limit var3) {
        return (T) executeSync(commands -> commands.zrangebyscore(var1, var2, var3));
    }

    @Override
    public Long zrem(String var1, Object... var2) {
        return executeSync(commands -> commands.zrem(var1, varargsConvert(var2)));
    }

    @Override
    public <T> T zcard(String var1) {
        return (T) executeSync(commands -> commands.zcard(var1));
    }

    @Override
    public <T> T hincr(String var1, String var2, long var3) {
        return (T) executeSync(commands -> commands.hincrby(var1, var2, var3));
    }

    /**
     * 初始化redis pool通过Apache Commons-pool2对redis连接资源池进行管理用于缓存Redis连接。
     * 因为Lettuce本身是基于Netty的异步驱动，在异步访问时并不需要创建连接池，但基于Servlet模型的同步访问时，连接池是有必要的
     * <p>
     * StatefulRedisConnection:线程安全的长连接，连接丢失会自动重连，直到调用close()关闭连接
     */
    @PostConstruct
    public void init() {
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        this.redisConnectionPool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(), poolConfig);
    }

    /**
     * 释放资源
     */
    @PreDestroy
    public void shutdown() {
        this.redisConnectionPool.close();
        this.redisClient.shutdown();
    }

    /**
     * 获取Redis连接，利用callback操作Redis，最后释放连接，并返回操作结果：
     * 仿照JdbcTemplate.execute(ConnectionCallback)方法，目的是减少样板代码
     * <p>
     * RedisCommands：Redis命令API接口 sync()代表同步
     *
     * @param callback
     * @param <T>
     * @return
     */
    public <T> T executeSync(SyncCommandCallback<T> callback) {
        try (StatefulRedisConnection<String, String> connection = redisConnectionPool.borrowObject()) {
            connection.setAutoFlushCommands(true);
            RedisCommands<String, String> commands = connection.sync();
            return callback.doInConnection(commands);
        } catch (Exception e) {
            //log.error("executeSync redis failed.", e);
            throw new RuntimeException(e);
        }
        /*StatefulRedisConnection<String, String> connection = null;
        RedisCommands<String, String> commands = null;
        try {
            connection = redisConnectionPool.borrowObject();
            connection.setAutoFlushCommands(true);
            commands = connection.sync();
            return callback.doInConnection(commands);
        } catch (Exception e) {
            //e.printStackTrace();
            connection.flushCommands();
            return callback.doInConnection(commands);
        }*/

    }

    /**
     * jackson序列化(默认会对String类型加双引号)
     *
     * @param var1
     * @return
     */
    private String writeValueAsString(Object var1) {
        return JSONObject.toJSONString(var1);
    }

    /*private String writeValueAsString(Object var1, boolean isJson) {
        try {
            String regexp = "\\\\";
            String v = objectMapper.writeValueAsString(var1);
            return isJson == true ? v.replaceAll(regexp, "") : v;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(var1 + "：转换异常");
        }
    }*/

    /**
     * 反序列化
     *
     * @param var1
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T readValue(String var1, Class<T> clazz) {
        try {
            return objectMapper.readValue(var1, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Object...转String...
     *
     * @param var1
     * @return
     */
    private static String[] varargsConvert(Object... var1) {
        List<Object> list = new ArrayList<>();
        for (Object o : var1) {
            list.add(o);
        }

        String[] strings = list.toArray(new String[list.size()]);
        return strings;
    }
}
