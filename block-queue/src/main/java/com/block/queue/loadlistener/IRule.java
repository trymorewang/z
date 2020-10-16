package com.block.queue.loadlistener;

import java.util.List;

/**
 * <p>
 *  轮询接口
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/15 10:36
 * @Version 1.0
 */
public interface IRule<V> {
    V choose(List<V> var1);
}
