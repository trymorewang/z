package com.miaosha.mapper;

import com.miaosha.common.entity.MiaoshaOrder;
import com.miaosha.common.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 邱润泽
 */
@Component
public interface OrderMapper {

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userNickName") long userNickName, @Param("goodsId") long goodsId);

    public long insert(OrderInfo orderInfo);

    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    public OrderInfo getOrderById(@Param("orderId") long orderId);

    public List<OrderInfo> selectOrderStatusByCreateTime(@Param("status") Integer status, @Param("createDate") String createDate);

    public int closeOrderByOrderInfo();

}
