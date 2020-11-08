package com.rocketmq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketmq.entity.WzOrder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderDao extends BaseMapper<WzOrder> {

    List<WzOrder> findAll();

    int updatePayStatusByOrderId(String orderId, Integer status);

    //int save(WzOrder wzOrder);


}
