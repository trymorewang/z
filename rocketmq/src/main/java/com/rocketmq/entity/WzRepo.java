package com.rocketmq.entity;

import lombok.Data;

@Data
public class WzRepo {
    private Integer id;
    private Integer goodId;
    private Integer buyNum;
    private String orderId;
    private Integer repoStatus;
}
