package com.miaosha2.config;

import com.miaosha2.core.RedisListener;
import com.miaosha2.utils.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/10/09 15:28
 * @Version 1.0
 */
@Configuration
public class BeanConfig {
    @Bean
    SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    RedisListener redisListener() {
        return new RedisListener();
    }
}
