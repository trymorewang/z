

# z

新建一个develop分支123

demo集合



# block-queue



##### 发布消息：

```java


@Autowired
private ZQueue1 zQueue;

@GetMapping("/push")
public String test() throws InterruptedException {

    JSONObject message = new JSONObject();
    //String tag = i % 2 == 0 ? "topic1" : "topic2";
    String tag = "topic1";
    message.put("tag", tag);
    message.put("body", "test");
    zQueue.push(message);

    return "{ \"status\": \"success\" }";

}
```



##### 消费消息：

```java

    
@Slf4j
@Component
@BlockQueueConsumer(topic = "topic1")
public class Topic1Consumer implements QueueListener<JSONObject> {
    @Override
    public void execute(JSONObject var1) {
        log.info("消费者1 - " + Thread.currentThread().getName() + "- get mssage:" + var1);
    }
}
```



# spring-schedule

##### 重写org.springframework.scheduling.ScheduledTaskRegistrar实现动态增删启停定时任务功能

数据表设计：

|      字段      |     类型     |          注释           |  空  |
| :------------: | :----------: | :---------------------: | :--: |
|     jobId      |   int(10)    |         任务id          |  否  |
|    beanName    | varchar(100) |        bean名称         |  否  |
|   methodName   | varchar(100) |        方法名称         |  否  |
|  methodParams  | varchar(255) |        方法参数         |  是  |
| cronExpression | varchar(255) |       cron表达式        |  否  |
|     remark     | varchar(500) |          备注           |  是  |
|   jobStatus    |  tinyint(3)  | 状态（1：正常 0：暂停） |  否  |
|   createTime   |   datatime   |        创建时间         |  否  |
|   updateTime   |   datatime   |        更新时间         |  否  |

前端提交字段：

```HTML
bean 名称：demoTask
方法  名称：taskWithParams
方法  参数：666
cron表达式：0/10 * * * * ?
状     态：正常/暂停	
备     注：test
```


# spring-websocket

实现socket的三种方式

1、使用Java提供的@ServerEndpoint注解实现
2、使用Spring提供的底层WebSocket API实现
3、使用STOMP消息实现

Spring Boot 整合 Websocket 官方文档：https://docs.spring.io/spring/docs/5.1.2.RELEASE/spring-framework-reference/web.html#websocket
服务器信息采集 oshi 使用：https://github.com/oshi/oshi

访问 http://localhost:8080/demo/server.html 查看效果



 # socket-io
 
 使用STOMP消息实现
 
 所谓STOMP(Simple Text Oriented Messaging Protocol)，就是在WebSocket基础之上提供了一个基于帧的线路格式（frame-based wire format）层。
 它对发送简单文本消息定义了一套规范格式（STOMP消息基于Text，当然也支持传输二进制数据），目前很多服务端消息队列都已经支持STOMP，
 比如：RabbitMQ、 ActiveMQ等。

访问 http://localhost:8080/demo/index.html 查看效果（可多个浏览器打开模拟多个用户效果）

# design-pattern

状态模式：

1、状态模式重点在各状态之间的切换从而做不同的事情，而策略模式更侧重于根据具体情况选择策略，并不涉及切换。

2、状态模式不同状态下做的事情不同，而策略模式做的都是同一件事，例如聚合支付平台，有支付宝、微信支付、银联支付，虽然策略不同，但最终做的事情都是支付，也就是说他们之间是可替换的。
反观状态模式，各个状态的同一方法做的是不同的事，不能互相替换。

状态模式封装了对象的状态，而策略模式封装算法或策略。因为状态是跟对象密切相关的，它不能被重用；而通过从Context中分离出策略或算法，我们可以重用它们。

在状态模式中，每个状态通过持有Context的引用，来实现状态转移；但是每个策略都不持有Context的引用，它们只是被Context使用。

测试结果

访问：http://127.0.0.1:8080/order?stateBeanId=alreadySignedOrderState

控制台输出：>> 切换已经签收状态

访问：http://127.0.0.1:8080/order?stateBeanId=inTransitOrderState

控制台输出：>>>切换为正在运送状态...

访问：http://127.0.0.1:8080/order?stateBeanId=shippedAlreadyOrderState

控制台输出：>>>切换为已经发货状态..

参考资料：https://mp.weixin.qq.com/s/KG_Xj-8UNuuGSK-lpWurqw


策略模式：

策略模式封装了算法或策略，侧重于根据具体的情况选择策略。

一个完整的策略模式要定义策略以及使用策略的上下文。

示例完整的策略模式如下图所示

┌───────────────────┐      ┌─────────────────┐
│PayContextStrategy │─ ─ ─>│    PayStrategy  │
└───────────────────┘      └─────────────────┘
                                ▲
                                │ ┌─────────────────────┐
                                ├─│  AliPayStrategy     │
                                │ └─────────────────────┘
                                │ ┌─────────────────────┐
                                ├─│  WxChatPayStrategy  │
                                │ └─────────────────────┘
                                │ ┌─────────────────────┐
                                └─│  UnionPayStrategy   │
                                  └─────────────────────┘

责任链模式：

     ┌─────────┐
     │ Request │
     └─────────┘
          │
┌ ─ ─ ─ ─ ┼ ─ ─ ─ ─ ┐
          ▼
│  ┌─────────────┐  │
   │ ProcessorA  │
│  └─────────────┘  │
          │
│         ▼         │
   ┌─────────────┐
│  │ ProcessorB  │  │
   └─────────────┘
│         │         │
          ▼
│  ┌─────────────┐  │
   │ ProcessorC  │
│  └─────────────┘  │
          │
└ ─ ─ ─ ─ ┼ ─ ─ ─ ─ ┘
          │
          ▼
使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。

在实际场景中，财务审批就是一个责任链模式。假设某个员工需要报销一笔费用，审核者可以分为：

Manager：只能审核1000元以下的报销；
Director：只能审核10000元以下的报销；
CEO：可以审核任意额度。


# 模板方法

定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

模板方法的主要思想是，定义一个操作的一系列步骤，对于某些暂时确定不下来的步骤，就留给子类去实现就好了，
这样不同的子类就能定义不同的步骤。

本示例是实现，从数据库读取数据保存在缓存中，不过不确定具体使用什么作为缓存的案例。

