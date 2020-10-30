package com.design.wrapper;

import java.util.concurrent.Callable;

public class Task implements Callable<Long> {

    private long num;
    public Task(long num) {
        this.num = num;
    }

    @Override
    public Long call() throws Exception {
        return ++num;
    }

    public static void main(String[] args) {
        Callable<Long> callable = new Task(100L);
        /**
         *
         * Task继承callable,Thread继承runable。
         *
         * 当参数为callable的时候通过调用实现runnable作为接口参数的方法时就会报错，一个办法是改写Task类，把实现的callable改写成runnable
         * 如果这个Task类有其他接口使用，会导致其他接口无法编译。另一个办法是不改写Task类，而是用一个Adapter/Wrapper(两者都代表适配器)，把这个
         * callable的接口改成runnable接口，这样就能正常编译。
         *
         * 注意：实现的接口都为抽象接口才能简单的实现适配器模式
         *
         */
        //Thread thread = new Thread(callable);
        Thread thread = new Thread(new RunnableWrapper(callable));
        thread.start();
    }
}
