package com.miaosha2.core;

import com.miaosha2.service.RedisService;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/30 16:46
 * @Version 1.0
 */
@Component
public class Producer {

    private final RedisService redisService;

    public Producer(RedisService redisService) {
        this.redisService = redisService;
    }

    public Boolean send(String key, String msg) {
        Long r = Long.valueOf(redisService.lPush(key, msg));
        if (r > 0) {
            return true;
        }
        return false;
    }
}
