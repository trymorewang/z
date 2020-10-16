package com.rocketmq.util;

import org.springframework.beans.BeanUtils;

import java.util.function.Supplier;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/08 10:34
 * @Version 1.0
 */
public class ObjectUtils {
    public static <S, T> T copyProperties(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <S, T> T copyProperties(S source, Supplier<T> targetSupplier) {
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public ObjectUtils() {
    }
}
