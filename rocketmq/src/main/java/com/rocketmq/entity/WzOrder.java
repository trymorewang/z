package com.rocketmq.entity;

import lombok.Data;

@Data
public class WzOrder {
    private Integer id;
    private String orderId;
    private Integer buyNum;
    private Integer goodId;
    private Integer userId;
    private Integer payStatus;
}
