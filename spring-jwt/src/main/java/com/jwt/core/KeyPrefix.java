package com.jwt.core;

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
    String compute(String var1);

    static KeyPrefix simple() {
        return (name) -> {
            return name + ":";
        };
    }
}
