package com.design.listener.enent.listener;

import com.design.listener.LotteryResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQEventListener implements EventListener {

    @Override
    public int listenerOrder() {
        return 1;
    }

    @Override
    public void doEvent(LotteryResult result) {
        log.info("记录用户 {} 摇号结果(MQ)：{}", result.getUid(), result.getMsg());
    }
}
