package com.design.listener.enent.listener;

import com.design.listener.LotteryResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEventListener implements EventListener {


    @Override
    public int listenerOrder() {
        return 0;
    }

    @Override
    public void doEvent(LotteryResult result) {
        log.info("给用户 {} 发送短信通知(短信)：{}", result.getUid(), result.getMsg());
    }
}
