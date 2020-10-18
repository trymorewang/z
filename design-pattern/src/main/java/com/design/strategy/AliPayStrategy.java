package com.design.strategy;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


/**
 * 支付宝支付策略
 */
@Slf4j
public class AliPayStrategy implements PayStrategy {

    @Override
    public void pay(BigDecimal account) {
      log.info("支付宝支付：{}", account);
    }
}
