package com.design.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券上下文
 * @Author: zhi.wang
 * @Date: 2021/4/9 19:04
 */
public class DiscountsContext {

    public static final String MJ = "MJ";
    public static final String LJ = "LJ";
    public static final String ZK = "ZK";

    //简单工厂的工厂容器
    private static Map<String, ICouponDiscount> discountsFactory = new HashMap<>();

    static  {
        discountsFactory.put(MJ, new MJCouponDiscount());
        discountsFactory.put(LJ, new LJCouponDiscount());
        discountsFactory.put(ZK, new ZKCouponDiscount());
    }

    public static ICouponDiscount getDiscounts(String key){
        if(discountsFactory.containsKey(key)){
            return discountsFactory.get(key);
        }
        return new NoneDiscount();
    }

}
