package com.design.strategy;

import java.math.BigDecimal;

/**
 * 所有实现此接口的优惠类型都可以分多种，比如满减 可以满100减10 也可以满100减20
 * @param <T>
 */
public interface ICouponDiscount<T> {
    /**
     * 优惠券金额计算
     * @param order   订单信息
     * @return           优惠后金额
     */
    BigDecimal discountAmount(Order order, Coupon coupon);
}
