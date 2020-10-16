package com.miaosha2.core;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/30 16:23
 * @Version 1.0
 */
public interface RedisHandler {
    /**
     * 队列名称
     */
    String queueName();

    /**
     * 队列消息内容
     */
    String consume (String msgBody);
}
