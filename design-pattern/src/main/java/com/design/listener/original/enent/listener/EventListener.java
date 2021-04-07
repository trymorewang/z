package com.design.listener.original.enent.listener;

import com.design.listener.original.LotteryResult;

public interface EventListener {

    int listenerOrder();

    void doEvent(LotteryResult result);
}
