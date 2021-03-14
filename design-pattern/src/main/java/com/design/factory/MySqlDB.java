package com.design.factory;

public class MySqlDB implements DB {
    @Override
    public void Connection() {
        System.out.println("打开mysql连接");
    }

    @Override
    public void execute() {
        System.out.println("执行mysql查询");
    }

    @Override
    public void close() {
        System.out.println("关闭mysql连接");
    }
}
