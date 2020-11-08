package com.rocketmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = {"com.rocketmq", "com.block.queue"})
@MapperScan(basePackages = "com.rocketmq.dao")
@SpringBootApplication
public class RocketmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketmqApplication.class, args);
	}

}
