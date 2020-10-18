package com.design.state;

/**
 * <p>
 *  context上下文
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/10/09 15:28
 * @Version 1.0
 */
public class StateContext {

    private OrderState orderState;

    public StateContext(OrderState orderState) {
        this.orderState = orderState;
    }

    public void switchStateOrder() {
        orderState.orderService();
    }
}
