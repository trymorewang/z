package com.design.listener.enent.listener;

import com.design.listener.LotteryResult;

public interface EventListener {

    int listenerOrder();

    void doEvent(LotteryResult result);
}
