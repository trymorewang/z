package com.design.strategy;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Order {
    //订单提交人的id
    private Long uId;

    //订单编号
    private String orderNo;

    //我们在购物时的总购物价格
    private BigDecimal prices;

    private String discountsKey;
}
