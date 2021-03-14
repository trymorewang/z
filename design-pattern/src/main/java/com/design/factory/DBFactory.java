package com.design.factory;

import java.lang.reflect.InvocationTargetException;

public class DBFactory {

    public static DB getInstance(String dbName) {
        DB db = null;
        try {
            db = (DB) Class.forName(dbName).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            //e.printStackTrace();
            System.out.println("未找到匹配的数据库");
        }
        return db;
    }
}
