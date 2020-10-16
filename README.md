

# z

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

1. Spring Boot 整合 Websocket 官方文档：https://docs.spring.io/spring/docs/5.1.2.RELEASE/spring-framework-reference/web.html#websocket
2. 服务器信息采集 oshi 使用：https://github.com/oshi/oshi

访问 http://localhost:8080/demo/server.html 查看效果