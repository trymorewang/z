package com.schedule.spring;

import org.springframework.stereotype.Component;

/**
 * <p>
 *  test
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:15
 * @Version 1.0
 */
@Component("demoTask")
public class DemoTask {
    public void taskWithParams(String params) {
        System.out.println("执行有参示例任务：" + params);
    }

    public void taskNoParams() {
        System.out.println("执行无参示例任务");
    }
}
