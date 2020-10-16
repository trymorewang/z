package com.miaosha.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="spring.redis")
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;//秒
    private String password;
    private int pollMinIdle;
    private int poolMaxIdle;
    private int poolMaxWait;//秒

}
