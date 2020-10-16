package com.miaosha2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(basePackages = "com.miaosha2.dao")
@SpringBootApplication
public class Miaosha2Application {

    public static void main(String[] args) {
        SpringApplication.run(Miaosha2Application.class, args);
    }

}
