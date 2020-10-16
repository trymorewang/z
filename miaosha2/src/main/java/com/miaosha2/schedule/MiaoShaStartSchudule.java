package com.miaosha2.schedule;

import com.miaosha2.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 *  秒杀是否开始监听器
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/28 17:21
 * @Version 1.0
 */
@Slf4j
@Component
public class MiaoShaStartSchudule implements InitializingBean {

    private final RedisService redisService;

    public MiaoShaStartSchudule(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     *
     * 临时增加秒杀商品怎么同步？
     * 方案1：（推）使用监听者模式，借助spring的ApplicationListener 自定义数据刷新事件，调用秒杀商品的管理api时发布商品刷新时间，监听之后重新刷新本地缓存
     * 方案2：（拉）定时任务新增一个查询当前秒杀活动的秒杀商品表数量方法，每次周期执行时与本地缓存的秒杀商品数量进行对比是否一致，不一致则进行同步刷新
     */
    //@Async("executor1")
    //@Scheduled(fixedDelay = 1000)
    @Scheduled(cron = "0/1 * * * * ?")
    public void method1() {
        String miaosha = "miaosha_1001";
        //判断key是否存在
        boolean exists = redisService.exists(miaosha + "_time");
        if (exists) {
            //已经修改了就不再修改
            Integer Start = Integer.valueOf(redisService.hget(miaosha + "_time", "Start"));
            if (Start == 0) {
                LocalDateTime date = LocalDateTime.now();
                String stringStart = redisService.hget(miaosha + "_time", "Start_Time");
                /*String regexp = "\"";
                LocalDateTime start = LocalDateTime.parse(stringStart.replaceAll(regexp, ""));*/
                LocalDateTime startTime = LocalDateTime.parse(stringStart);
                if (date.isEqual(startTime) || date.isAfter(startTime)) {
                    redisService.hset(miaosha + "_time", "Start", 1);
                    log.info("商品秒杀开始。。。");
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        log.info("秒杀是否开始监听器初始化成功");
    }

}
