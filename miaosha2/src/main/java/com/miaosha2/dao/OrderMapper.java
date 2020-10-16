package com.miaosha2.dao;

import com.miaosha2.entity.MiaoshaOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component
public interface OrderMapper {

    int insert(MiaoshaOrder miaoshaOrder);

    int closeOrder(@Param("orderId") String orderId);

    MiaoshaOrder get(@Param("orderId") String orderId);
}
