package com.design.strategy;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Coupon {

    //优惠劵类型(0-满减；1-立减)
    private Integer type;
    //满足多少金额使用（仅type=1-满减）
    private BigDecimal meetAmount;
    //优惠金额
    private BigDecimal discountaAmount;

}
