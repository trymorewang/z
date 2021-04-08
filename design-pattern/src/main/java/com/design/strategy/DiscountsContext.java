package com.design.strategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DiscountsContext<T> {

    public static final String MJ = "MJ";
    public static final String ZJ = "ZJ";
    public static final String ZK = "ZK";

    //简单工厂的工厂容器
    private static Map<String, ICouponDiscount> discountsFactory = new HashMap<>();

    static  {
        discountsFactory.put(MJ, new MJCouponDiscount());
        discountsFactory.put(ZJ, new ZJCouponDiscount());
        discountsFactory.put(ZK, new ZKCouponDiscount());
    }

    public static ICouponDiscount getDiscounts(String key){
        if(discountsFactory.containsKey(key)){
            return discountsFactory.get(key);
        }
        return new NoneDiscount();
    }

}
