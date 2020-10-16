package com.miaosha2.functional;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * <p>
 *  回调函数
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/24 13:57
 * @Version 1.0
 */
@FunctionalInterface
public interface SyncCommandCallback<T> {

    // 在此操作Redis:
    T doInConnection(RedisCommands<String, String> commands);

}
