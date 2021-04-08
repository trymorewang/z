package com.design.strategy;

import java.math.BigDecimal;

public class ZJCouponDiscount implements ICouponDiscount<Double>  {

    /**
     * 直减计算
     * 1. 使用商品价格减去优惠价格
     * 2. 最低支付金额1元
     */
    public BigDecimal discountAmount(Order order) {
        Double couponInfo = 10.0;
        BigDecimal skuPrice = order.getPrices();
        BigDecimal discountAmount = skuPrice.subtract(new BigDecimal(couponInfo));
        if (discountAmount.compareTo(BigDecimal.ZERO) < 1) return BigDecimal.ONE;
        return discountAmount;
    }

}
