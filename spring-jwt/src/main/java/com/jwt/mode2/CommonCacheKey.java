package com.jwt.mode2;


import com.jwt.abstracts.AbstractCacheKeyPrefix;
import com.jwt.functional.KeyPrefix;
import com.jwt.interfaces.CacheKeyPrefix;


public class CommonCacheKey extends AbstractCacheKeyPrefix {

    private static final String KEY = "JWT";

    public String key(Object k) {
        return prefix(KeyPrefix.simple()) + k;
    }

    private String prefix(KeyPrefix keyPrefix) {
        return keyPrefix.compute(KEY, null);
    }

    private String prefix(KeyPrefix keyPrefix, String prefix) {
        return keyPrefix.compute(KEY, prefix);
    }

    public static void main(String[] args) {
        CacheKeyPrefix cacheKeyPrefix = new CommonCacheKey();
        String v = cacheKeyPrefix.key("123");
        System.out.println(v);
    }


}
