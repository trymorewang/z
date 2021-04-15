package com.design.strategy;

import java.math.BigDecimal;
import java.util.Map;

public class MJCouponDiscount implements ICouponDiscount  {

    /**
     * 满减计算
     * 1. 判断满足x元后-n元，否则不减
     * 2. 最低支付金额1元
     */
    @Override
    public BigDecimal discountAmount(Order order, Coupon coupon) {
        BigDecimal x = coupon.getMeetAmount();
        BigDecimal n = coupon.getDiscountaAmount();

        BigDecimal skuPrice = order.getPrices();
        // 小于商品金额条件的，直接返回商品原价
        if (skuPrice.compareTo(x) < 0) return skuPrice;
        // 减去优惠金额判断
        BigDecimal discountAmount = skuPrice.subtract(n);
        if (discountAmount.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ONE;

        return discountAmount;
    }
}
