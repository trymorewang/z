package com.miaosha2.service;

import io.lettuce.core.KeyValue;
import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.ScriptOutputType;

import java.util.Map;

/**
 * <p>
 *  redis操作接口
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */

public interface RedisService {

    /**
     * 添加一个元素
     * @param var1
     * @param var2
     * @param <T>
     * @return
     */
    <T> T set(String var1, T var2);

    /**
     *
     * 添加一个元素 key 不存在时设置
     * @param var1
     * @param var2 秒
     * @param var3
     * @param <T>
     * @return
     */
    <T> T setex(String var1, long var2, T var3);

    /**
     * 添加一个元素
     * @param var1
     * @param var2
     * @param <T>
     * @return
     */
    <T> T setnx(String var1, String var2);

    /**
     * 获取一个元素
     * @param var1
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T get(String var1, Class<T> clazz);

    /**
     * 将脚本缓存
     * @param var1
     * @return
     */
    <T> T scriptLoad(String var1);

    /**
     * 调用脚本比直接调用eval()节约带宽
     * @param var1
     * @param var2
     * @param var3
     * @param var4
     * @param <T>
     * @return
     */
    <T> T evalsha(String var1, ScriptOutputType var2, String[] var3, String... var4);

    /**
     * 往list左边push一个元素
     * @param var1
     * @param var2
     * @return
     */
    Long lPush(String var1, Object... var2);

    /**
     * 获取list长度
     * @param var1
     * @return
     */
    Long llen(String var1);

    /**
     * 从list右边移除一个元素(阻塞)
     * @param var1
     * @param var2
     * @return
     */
    <T> T brpop(long var1, String var2);

    /**
     * 递增+1
     * @param var1
     * @return
     */
    Long incr(String var1);

    /**
     * 查看key是否存在
     * @param ks
     * @return
     */
    Boolean exists(String... ks);

    Boolean hset(String var1, String var2, Object var3);

    Long hset(String var1, Map<String, String> var2);

    <T> T hget(String var1, String var2);

    <T> T zadd(String var1, Double var2, T var3);

    <T> T zrangebyscore(String var1, Range<Number> var2, Limit var3);

    Long zrem(String var1, Object... var2);

    <T> T zcard(String var1);

    <T> T hincr(String var1, String var2, long var3);

}