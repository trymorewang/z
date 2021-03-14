package com.design.factory;

import java.io.IOException;
import java.util.Properties;

public class DBTest {

    public static void main(String[] args) throws IOException {
        Properties pro = init.getPro();
        DB db1 = DBFactory.getInstance(pro.getProperty("mysql"));
        db1.close();
        DB db2 = DBFactory.getInstance(pro.getProperty("oracle"));
        db2.close();
    }
}
