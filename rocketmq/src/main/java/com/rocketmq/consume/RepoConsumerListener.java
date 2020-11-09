package com.rocketmq.consume;

import com.rocketmq.dao.WarehouseDao;
import com.rocketmq.entity.RepoRecord;
import com.rocketmq.entity.WzWarehouse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * <p>
 * 库存监听事务消息
 * <p>
 * 假设多个消息流传 其中一个步骤出现失败如何回滚
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/10 16:13
 * @Version 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "repo-pay-group", topic = "Repo-Success")
public class RepoConsumerListener implements RocketMQListener<RepoRecord> {


    @Autowired
    WarehouseDao warehouseDao;

    @Override
    public void onMessage(RepoRecord repoRecord) {

        System.out.println("接受到库存处理完成的消息");
        WzWarehouse wzWarehouse = new WzWarehouse();
        wzWarehouse.setDeliveryStatus(0);
        wzWarehouse.setLogisticsId(UUID.randomUUID().toString());
        wzWarehouse.setOrderId(repoRecord.getOrderId());
        warehouseDao.insert(wzWarehouse);

    }
}
