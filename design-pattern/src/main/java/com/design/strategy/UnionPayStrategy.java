package com.design.strategy;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class UnionPayStrategy implements PayStrategy {

    @Override
    public void pay(BigDecimal account) {
        log.info("银联支付：{}", account);
    }
}
