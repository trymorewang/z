package com.design.listener.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SmsListener implements ApplicationListener<OrderCreateEvent> {

    @Async
    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        //发送短信
        System.out.println(Thread.currentThread().getName() + ":" + event.getContentList().get(0) + ",您的订单:" + event.getContentList().get(1) + "创建成功! ----by sms");
    }
}
