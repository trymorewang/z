package com.jwt.mode1;


import com.jwt.abstracts.AbstractCacheKeyPrefix;
import com.jwt.interfaces.CacheKeyPrefix;


public class CommonCacheKey extends AbstractCacheKeyPrefix {

    public CommonCacheKey() {
        super();
    }

    public CommonCacheKey(String prefix) {
        super(prefix + ":%s");
    }

    public String key(Object value) {
        return String.format(prefix(), value);
    }

    public static void main(String[] args) {
        //CacheKeyPrefix cacheKeyPrefix = new CommonCacheKey();
        CacheKeyPrefix cacheKeyPrefix = new CommonCacheKey("wz");
        String k =  cacheKeyPrefix.key("123");
        System.out.println(k);
    }

}
