package com.rocketmq.entity;

import lombok.Data;

@Data
public class WarehouseRecord {
    private String orderId;
    private String logisticsId;
    private Integer deliveryStatus;
}
