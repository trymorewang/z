package com.jwt.abstracts;

import com.jwt.interfaces.CacheKeyPrefix;


public abstract class AbstractCacheKeyPrefix implements CacheKeyPrefix {

    private static final String FORMAT = ":%s";
    private static final String KEY = "JWT" + FORMAT;

    /**
     * 缓存key前缀，建议为以业务名(或数据库名)为前缀(防止key冲突),用冒号分隔,如user:id:1(user为模块名)
     * 当key较多时,在保证语言的前提下控制key的长度,如user:{uid}:friends:messages:{mid}简化为u:{uid}:fr:m:{mid}
     */
    private String prefix;

    public AbstractCacheKeyPrefix() {
        this.prefix = KEY;
    }

    public AbstractCacheKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String prefix() {
        return prefix.substring(0, FORMAT.length());
    }

    @Override
    public String key(String key) {
        return String.format(prefix, key);
    }
}
