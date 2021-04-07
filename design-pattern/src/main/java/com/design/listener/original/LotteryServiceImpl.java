package com.design.listener.original;

import java.util.Date;

public class LotteryServiceImpl extends LotteryService{

    private final MinibusTargetService minibusTargetService = new MinibusTargetService();

    @Override
    protected LotteryResult doDraw(String uid) {
        // 摇号
        String lottery = minibusTargetService.lottery(uid);
        // 结果
        return new LotteryResult(uid, lottery, new Date());
    }
}
