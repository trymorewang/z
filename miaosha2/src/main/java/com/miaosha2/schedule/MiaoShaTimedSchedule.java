package com.miaosha2.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaosha2.dao.GoodsMapper;
import com.miaosha2.dao.OrderMapper;
import com.miaosha2.entity.MiaoshaGoods;
import com.miaosha2.entity.MiaoshaOrder;
import com.miaosha2.service.RedisService;
import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.ScriptOutputType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 超时订单监听器
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/28 18:47
 * @Version 1.0
 */
@Slf4j
@Component
public class MiaoShaTimedSchedule implements InitializingBean {


    private final RedisService redisService;

    private final GoodsMapper goodsMapper;

    private final OrderMapper orderMapper;


    private static List<Long> goods = new ArrayList<>();

    public MiaoShaTimedSchedule(RedisService redisService, GoodsMapper goodsMapper, OrderMapper orderMapper) {
        this.redisService = redisService;
        this.goodsMapper = goodsMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * <p>
     * lettuce hash结构没有原子递减函数，自己实现思路如下：
     * 1、需要调用递减函数的地方，使用单线程执行(创建一个只有一个线程的线程池)，先获取，计算之后再重新set
     * 2、lua脚本
     * </p>
     */
    //@Async("executor1")
    //@Scheduled(fixedDelay = 1000)
    @Scheduled(cron = "0/5 * * * * ?")
    public void method1() {

        if (goods.isEmpty()) {
            List<MiaoshaGoods> miaoshaGoodsList = goodsMapper.listGoodsVo();
            miaoshaGoodsList.forEach(e -> {
                Long goodsId = e.getGoodsId();
                goods.add(goodsId);
            });
        }

        goods.forEach(e -> {
            timed(e);
        });


    }

    private void timed(Long goodsId) {
        String key = "miaosha_" + goodsId;
        String timeQueue = key + "_time_queue";

        long now = System.currentTimeMillis();
        //通过scope构造取值范围
        Range<Number> numberRange = Range.from(Range.Boundary.including(0), Range.Boundary.including(now));
        //在符合条件区间取5个
        List<String> stringList = redisService.zrangebyscore(timeQueue, numberRange, Limit.create(0, 5));
        if (!stringList.isEmpty()) {
            String[] strings = stringList.toArray(new String[stringList.size()]);
            log.info("即将删除过期订单：" + stringList);
            log.info("即将删除过期订单长度：" + stringList.size());
            log.info("即将删除过期数组长度：" + strings.length);
            redisService.zrem(timeQueue, strings);

            //允许请求数清零(剩余库存需要重新售卖)
            redisService.set(key + "_access", 0);
            //库存回填
            /*String bookedKey = "Booked";
            Integer bookeds = Integer.valueOf(redisService.hget(miaosha, bookedKey));
            bookeds = bookeds - strings.length;*/
            //返回false却操作成功了
            //Boolean booked = redisService.hset(miaosha, bookedKey, bookeds);

            //模拟并发
            /*final CountDownLatch latch = new CountDownLatch(1);
            for (int i = 0; i < 5; i++) {
                new Thread(() -> {
                    //lua实现
                    Boolean booked = hdecr(String.valueOf(strings.length));
                    log.info("库存回填结果：{}", booked);
                }).start();
            }
            latch.countDown();*/
            //lua实现
            Boolean booked = hdecr(goodsId, strings.length);
            log.info("库存回填结果：{}", booked);

            //通过延时队列订单号查询数据表状态，如果已经支付不做处理，未支付更新订单状态
            stringList.forEach(e -> {
                e = trimSlash(e);
                e = trimFirstAndLastChar(e, "\"");
                MiaoshaOrder miaoshaOrder = JSONObject.parseObject(e, MiaoshaOrder.class);
                MiaoshaOrder mo = orderMapper.get(miaoshaOrder.getOrderId());
                if (mo.getStatus().equals(0)) {
                    orderMapper.closeOrder(miaoshaOrder.getOrderId());
                }
            });
        }
    }

    /**
     * 去掉斜杆
     *
     * @param var1
     * @return
     */
    private String trimSlash(String var1) {
        String regexp = "\\\\";
        return var1.replaceAll(regexp, "");
    }

    /**
     * 去除首尾指定字符
     *
     * @param str     字符串
     * @param element 指定字符
     * @return
     */
    public static String trimFirstAndLastChar(String str, String element) {
        boolean beginIndexFlag;
        boolean endIndexFlag;
        do {
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
            str = str.substring(beginIndex, endIndex);
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }

    private Boolean hdecr(Long goodsId, int var1) {
        String lua =
                "local n = tonumber(ARGV[1])" +
                        "if not n  or n == 0 then" +
                        "    return n       " +
                        "end                " +
                        "local vals = redis.call('HMGET', KEYS[1], 'Total', 'Booked');" +
                        "local total = tonumber(vals[1])" +
                        "local blocked = tonumber(vals[2])" +
                        "if not total or not blocked then" +
                        "    return 0       " +
                        "end                " +
                        "if blocked + n <= total and blocked + n >= 0 then" +
                        "    redis.call('HINCRBY', KEYS[1], 'Booked', n)" +
                        "    return n;   " +
                        "end                " +
                        "return n";

        String luaScript = redisService.scriptLoad(lua);

        String[] keys = new String[]{"miaosha_" + goodsId};
        String arg1 = "-" + var1;
        String[] args = new String[]{arg1};

        Long result = redisService.evalsha(luaScript, ScriptOutputType.INTEGER, keys, args);
        //System.out.println(result);
        return Long.valueOf(arg1).equals(result);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("待支付订单过期监听器初始化成功");
    }
}
