package com.design.strategy;

import java.math.BigDecimal;

/**
 * 优惠券使用类
 * @Author: zhi.wang
 * @Date: 2021/4/9 19:04
 */
public class DiscountsUse {
    //优惠卷的使用方法，通过我们的优惠卷工厂来获取我们的具体的优惠卷使用算法
    public BigDecimal useCoupons(Order order, Coupon coupon){
        return DiscountsContext.getDiscounts(order.getDiscountsKey()).discountAmount(order, coupon);
    }
}
