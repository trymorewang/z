package com.design.chain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *   责任链的模式有很多变种
 *
 *   此实现方式是找到某个handle来处理request，还有的方式：
 *   1、通过某个handle手动调用下一个handle来传递request
 *   2、每个handle都有机会处理request，这种责任链通常称为拦截器或者过滤器，目的不是找到一个handle处理掉request而是每个handle都做一些工作
 *   比如：
 *      记录日志；
 *      检查权限；
 *      准备相关资源；
 *      ...
 * </p>
 *
 * @author Zhi.Wang
 * @version 1.0
 * @date 2020-10-16
 */
public class HandlerChain {

    // 持有所有Handler:
    private List<Handler> handlers = new ArrayList<>();

    public void addHandler(Handler handler) {
        this.handlers.add(handler);
    }

    public boolean process(Request request) {
        // 依次调用每个Handler:
        for (Handler handler : handlers) {
            Boolean r = handler.process(request);
            if (r != null) {
                // 如果返回TRUE或FALSE，处理结束:
                System.out.println(request + " " + (r ? "Approved by " : "Denied by ") + handler.getClass().getSimpleName());
                return r;
            }
        }
        throw new RuntimeException("Could not handle request: " + request);
    }

    public static void main(String[] args) {

        // Handler添加的顺序很重要，如果顺序不对，处理的结果可能就不是符合要求的
        // 构造责任链:
        HandlerChain chain = new HandlerChain();
        chain.addHandler(new ManagerHandler());
        chain.addHandler(new DirectorHandler());
        chain.addHandler(new CEOHandler());
        // 处理请求:
        chain.process(new Request("Bob", new BigDecimal("123.45")));
        chain.process(new Request("Alice", new BigDecimal("1234.56")));
        chain.process(new Request("Bill", new BigDecimal("12345.67")));
        chain.process(new Request("John", new BigDecimal("123456.78")));
    }
}
