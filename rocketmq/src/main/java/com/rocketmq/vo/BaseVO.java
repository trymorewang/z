package com.rocketmq.vo;


import com.rocketmq.util.ObjectUtils;

import java.util.function.Supplier;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/08 10:33
 * @Version 1.0
 */
public abstract class BaseVO {
    public BaseVO() {
    }

    public <T> T copyProperties(Supplier<T> supplier) {
        return ObjectUtils.copyProperties(this, supplier);
    }

    public <T> T copyProperties(T t) {
        return ObjectUtils.copyProperties(this, t);
    }
}
