package com.design.listener;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LotteryResult {
    private String uid;
    private String msg;
    private Date time;
}
