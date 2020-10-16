package com.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.rocketmq", "com.block.queue"})
public class RocketmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(RocketmqApplication.class, args);
	}

}
