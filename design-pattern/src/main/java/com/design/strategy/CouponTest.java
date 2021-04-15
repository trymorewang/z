package com.design.strategy;

import java.math.BigDecimal;

/**
 * @Author: zhi.wang
 * @Date: 2021/4/9 19:04
 */
public class CouponTest {

    public static void main(String[] args) {
        BigDecimal price1 = new DiscountsUse().useCoupons(Order.builder().orderNo("123").discountsKey(DiscountsContext.MJ).prices(new BigDecimal(50)).build(), Coupon.builder().type(1).meetAmount(new BigDecimal(100)).discountaAmount(new BigDecimal(10)).build());
        BigDecimal price2 = new DiscountsUse().useCoupons(Order.builder().orderNo("123").discountsKey(DiscountsContext.LJ).prices(new BigDecimal(100)).build(), Coupon.builder().type(2).discountaAmount(new BigDecimal(10)).build());
        //BigDecimal price3 = new DiscountsUse().useCoupons(Order.builder().orderNo("123").discountsKey(DiscountsContext.ZK).prices(new BigDecimal(100)).build());
        System.out.println(price1);
        System.out.println(price2);
        //System.out.println(price3);
    }
}
