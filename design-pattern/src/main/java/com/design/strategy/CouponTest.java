package com.design.strategy;

import java.math.BigDecimal;

public class CouponTest {

    public static void main(String[] args) {
        BigDecimal price1 = new DiscountsUse().useCoupons(Order.builder().orderNo("123").discountsKey(DiscountsContext.MJ).prices(new BigDecimal(100)).build());
        BigDecimal price2 = new DiscountsUse().useCoupons(Order.builder().orderNo("123").discountsKey(DiscountsContext.ZJ).prices(new BigDecimal(100)).build());
        BigDecimal price3 = new DiscountsUse().useCoupons(Order.builder().orderNo("123").discountsKey(DiscountsContext.ZK).prices(new BigDecimal(100)).build());
        System.out.println(price1);
        System.out.println(price2);
        System.out.println(price3);
    }
}
