package com.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebsocketApplication.class, args);
    }

}
