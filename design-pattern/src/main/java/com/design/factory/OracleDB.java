package com.design.factory;

public class OracleDB implements DB {
    @Override
    public void Connection() {
        System.out.println("打开oracle连接");
    }

    @Override
    public void execute() {
        System.out.println("执行oracle查询");
    }

    @Override
    public void close() {
        System.out.println("关闭oracle连接");
    }
}
