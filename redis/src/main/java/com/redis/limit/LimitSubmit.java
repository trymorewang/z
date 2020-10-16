package com.redis.limit;

import java.lang.annotation.*;

/**
 * <p>
 *  防重提交注解
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/12 10:54
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LimitSubmit {
    String key() ;
    /**
     * 默认 10s
     */
    int limit() default 10;

    /**
     * 请求完成后 是否一直等待
     * true则等待
     * @return
     */
    boolean needAllWait() default true;
}
