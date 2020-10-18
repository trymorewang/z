package com.design.chain;

import java.math.BigDecimal;

public class DirectorHandler implements Handler {

    @Override
    public Boolean process(Request request) {
        // 如果超过1000元，处理不了，交下一个处理:
        if (request.getAmount().compareTo(BigDecimal.valueOf(10000)) > 0) {
            return null;
        }
        return true;
    }
}
