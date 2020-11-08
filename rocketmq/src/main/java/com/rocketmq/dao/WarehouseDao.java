package com.rocketmq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketmq.entity.WzOrder;
import com.rocketmq.entity.WzWarehouse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WarehouseDao extends BaseMapper<WzWarehouse> {

    List<WzWarehouse> findAll();

    void updateDeliveryStatusByOrderId(String orderId, int status);
}
