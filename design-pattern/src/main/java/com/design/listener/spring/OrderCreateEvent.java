package com.design.listener.spring;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Data
public class OrderCreateEvent extends ApplicationEvent {

    private String name;

    //消息参数
    private List<String> contentList;

    public OrderCreateEvent(Object source, String name, List<String> contentList) {
        super(source);
        this.name = name;
        this.contentList = contentList;
    }
}
