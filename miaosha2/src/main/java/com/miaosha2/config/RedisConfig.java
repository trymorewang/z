package com.miaosha2.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * <p>
 *  redis配置类
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/24 13:38
 * @Version 1.0
 */
@Data
@Configuration
@ConfigurationProperties("spring.redis")
public class RedisConfig {
    private String host;
    private int port;
    private String password;
    private int database;

    @Bean
    RedisClient redisClient() {
        RedisURI uri = RedisURI.Builder.redis(this.host, this.port)
                .withPassword(this.password)
                .withDatabase(this.database)
                .withTimeout(Duration.ofSeconds(5))
                .build();
        return RedisClient.create(uri);
    }
}
