package com.design.strategy;

import java.math.BigDecimal;

public class DiscountsUse {
    //优惠卷的使用方法，通过我们的优惠卷工厂来获取我们的具体的优惠卷使用算法
    public BigDecimal useCoupons(Order order){
        return DiscountsContext.getDiscounts(order.getDiscountsKey()).discountAmount(order);
    }
}
