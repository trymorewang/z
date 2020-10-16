package com.schedule.spring;

import java.util.concurrent.ScheduledFuture;

/**
 * <p>
 *  定时任务线程池执行结果
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:12
 * @Version 1.0
 */
public class ScheduledTask {

    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
