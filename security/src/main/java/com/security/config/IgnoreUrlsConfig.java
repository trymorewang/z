package com.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  用户配置不需要保护的路径
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/04 11:46
 * @Version 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "secure.ignored")
@Component
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();

}
