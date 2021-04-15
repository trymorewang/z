package com.design.strategy;

import java.math.BigDecimal;

public class NoneDiscount implements ICouponDiscount{
    @Override
    public BigDecimal discountAmount(Order order, Coupon coupon) {
        return null;
    }
}
