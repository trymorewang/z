package com.design.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShippedAlreadyOrderState implements OrderState {

    @Override
    public String orderService() {
        log.info(">>>切换为已经发货状态..");
        return "已经发货..";
    }
}
