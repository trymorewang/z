package com.security.annotation;

import java.lang.annotation.*;

/**
 * <p>
 *     权限注解
 * </p>
 *
 * @author Zhi.Wang
 * @version 1.0
 * @date 2020-08-03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MallPermission {
    String hasRole() default "";
    String hasResource() default "";
}
