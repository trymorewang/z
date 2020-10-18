package com.design.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InTransitOrderState implements OrderState {

    @Override
    public String orderService() {
        log.info(">>>切换为正在运送状态.");
        return "success";
    }
}
