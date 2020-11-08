package com.rocketmq.service;

import com.rocketmq.dao.WarehouseDao;
import com.rocketmq.entity.WzWarehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    WarehouseDao warehouseDao;

    public final int DELIVERY_DONE = 1;

    /**
     *  检查仓储是否存在并且状态是已发货
     **/
    public boolean checkWarehouseSuccess(String orderId){
        List<WzWarehouse> allOrders = warehouseDao.findAll();
        return  allOrders.stream()
                .anyMatch(order -> order.getOrderId().equals(orderId) && order.getDeliveryStatus() == DELIVERY_DONE);
    }

    /**
     *  更新仓储状态为已发货
     **/
    public void updateDeliveryStatusByOrderId(String orderId){
        warehouseDao.updateDeliveryStatusByOrderId(orderId, DELIVERY_DONE);
    }

    /**
     *  生成仓储记录，状态默认是未发货
     **/

    public void save(String orderId, String logisticsId) {

        WzWarehouse warehouse = new WzWarehouse();
        warehouse.setOrderId(orderId);
        warehouse.setLogisticsId(logisticsId);
        warehouse.setDeliveryStatus(0);
        warehouseDao.insert(warehouse);
    }
}
