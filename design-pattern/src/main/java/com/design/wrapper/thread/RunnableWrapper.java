package com.design.wrapper.thread;

import java.util.concurrent.Callable;

public class RunnableWrapper implements Runnable {

    //持有待装换接口引用
    private Callable<?> callable;

    public RunnableWrapper(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            //将指定接口调用委托给给转换接口调用
            Long r = (Long) callable.call();
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
