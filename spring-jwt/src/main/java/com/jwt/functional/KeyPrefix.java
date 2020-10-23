package com.jwt.functional;

import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/10/22 17:58
 * @Version 1.0
 */
@FunctionalInterface
public interface KeyPrefix {
    String compute(String var1, String var2);

    static KeyPrefix simple() {
        return (name, v) -> {
            return StringUtils.isEmpty(v) ? (name + ":") : name + v;
        };
    }
}
