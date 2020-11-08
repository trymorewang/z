package com.rocketmq.entity;

import lombok.Data;

@Data
public class RepoRecord {
    private Integer goodId;
    private Integer buyNum;
    private String orderId;
    private Integer repoStatus;
}
