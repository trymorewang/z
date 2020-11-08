package com.rocketmq.entity;

import lombok.Data;

@Data
public class WzWarehouse {

    private Integer id;
    private String orderId;
    private String logisticsId;
    private Integer deliveryStatus;
}
