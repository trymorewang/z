package com.jwt.interfaces;

/**
 * 缓存key通用设置
 *
 * @author Wilson
 * @date 2019/9/23
 **/
public interface CacheKeyPrefix<K> {

    /**
     * 获取业务缓存前缀
     */
    String prefix();

    /**
     * 获取业务缓存key
     * @param key
     * @return
     */
    String key(String key);
}
