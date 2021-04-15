package com.design.strategy;

import java.math.BigDecimal;

public class LJCouponDiscount implements ICouponDiscount<Double>  {

    /**
     * 立减计算
     * 1. 使用商品价格减去优惠价格
     * 2. 最低支付金额1元
     */
    @Override
    public BigDecimal discountAmount(Order order, Coupon coupon) {
        Double couponInfo = 10.0;
        BigDecimal skuPrice = order.getPrices();
        BigDecimal discountAmount = skuPrice.subtract(new BigDecimal(couponInfo));
        if (discountAmount.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ONE;
        return discountAmount;
    }

}
