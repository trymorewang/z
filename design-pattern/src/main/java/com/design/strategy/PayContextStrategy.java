package com.design.strategy;

import java.math.BigDecimal;

/**
 * <p>
 *  策略上下文
 * </p>
 *
 * @author Zhi.Wang
 * @version 1.0
 * @date 2020-10-16
 */
public class PayContextStrategy {

    private PayStrategy payStrategy;

    public void setPayStrategy(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    private void swichPay(BigDecimal account) {
        payStrategy.pay(account);
    }

    public static void main(String[] args) {
        PayContextStrategy contextStrategy = new PayContextStrategy();

        //使用支付宝支付
        contextStrategy.setPayStrategy(new AliPayStrategy());
        contextStrategy.swichPay(new BigDecimal(100));

        //使用微信支付
        contextStrategy.setPayStrategy(new WxChatPayStrategy());
        contextStrategy.swichPay(new BigDecimal(100));

        //使用银联支付
        contextStrategy.setPayStrategy(new UnionPayStrategy());
        contextStrategy.swichPay(new BigDecimal(100));
    }
}
