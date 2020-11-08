package com.rocketmq.entity;

import lombok.Data;

@Data
public class OrderRecord {

    private Integer userId;
    private String orderId;
    private Integer buyNum;
    private Integer payStatus;
    private Integer goodId;
}
