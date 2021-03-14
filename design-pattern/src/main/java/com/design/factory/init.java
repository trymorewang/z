package com.design.factory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class init {
    public static Properties getPro() throws IOException {
        Properties pro = new Properties();
        Resource classPathResource = new ClassPathResource("db.properties");
        File f = classPathResource.getFile();
        if (f.exists()) {
            pro.load(new FileInputStream(f));
        } else {
            pro.setProperty("mysql", "com.design.factory.MySqlDB");
            pro.setProperty("oracle", "com.design.factory.OracleDB");
            pro.store(new FileOutputStream(f), "DB CLASS");
        }
        return pro;
    }
}
