package com.design.state.service;

public class OrderService {

    /**
     * 需要重构的代码
     * @param state
     * @return
     */
    public String orderState(String state) {
        if (state.equals("0")) {
            return "已经发货";
        }
        if (state.equals("1")) {
            return "正在运送中...";
        }
        if (state.equals("2")) {
            return "正在派送中...";
        }
        if (state.equals("3")) {
            return "已经签收";
        }
        if (state.equals("4")) {
            return "拒绝签收";
        }
        if (state.equals("5")) {
            return "订单交易失败";
        }
        return "未找到对应的状态";
    }
}
