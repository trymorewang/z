package com.rocketmq.consume;

import com.rocketmq.dao.WarehouseDao;
import com.rocketmq.entity.RepoRecord;
import com.rocketmq.entity.WarehouseRecord;
import com.rocketmq.entity.WzWarehouse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "repo-pay-group", topic = "Repo-Success")
public class WarehouseConsumerListener implements RocketMQListener<RepoRecord> {

    @Autowired
    private WarehouseDao warehouseDao;

    @Override
    public void onMessage(RepoRecord repoRecord) {
        WzWarehouse wzWarehouse = new WzWarehouse();
        wzWarehouse.setDeliveryStatus(0);
        wzWarehouse.setLogisticsId(UUID.randomUUID().toString());
        wzWarehouse.setOrderId(repoRecord.getOrderId());
        warehouseDao.insert(wzWarehouse);
    }
}
