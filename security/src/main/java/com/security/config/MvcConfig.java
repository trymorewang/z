package com.security.config;

import com.security.interceptor.MallPermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <p>
 *  拦截器添加到mvc配置
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 15:19
 * @Version 1.0
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MallPermissionInterceptor mallPermissionInterceptor() {
        return new MallPermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mallPermissionInterceptor()).excludePathPatterns("/static/*")
                .excludePathPatterns("/error").addPathPatterns("/**");
    }

}
