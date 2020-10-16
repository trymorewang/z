package com.miaosha2.controller;

import com.alibaba.fastjson.JSONObject;
import com.miaosha2.constant.PlatformConstants;
import com.miaosha2.dao.GoodsMapper;
import com.miaosha2.entity.Message;
import com.miaosha2.core.Producer;
import com.miaosha2.entity.MiaoshaGoods;
import com.miaosha2.entity.MiaoshaOrder;
import com.miaosha2.service.RedisService;
import io.lettuce.core.ScriptOutputType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/24 12:27
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("redis")
public class TestController implements SmartInitializingSingleton {

    private static final String miaosha = "miaosha";

    @Autowired
    private RedisService redisService;

    @Autowired
    private Producer producer;

    @Autowired
    private GoodsMapper goodsMapper;

    @GetMapping("hset")
    public Boolean hset() {
        String bookedKey = "Booked";
        Integer bookeds = Integer.valueOf(redisService.hget("miaosha_1001", bookedKey));
        bookeds = bookeds - 1;
        return redisService.hset(miaosha, bookedKey, bookeds);
    }

    @GetMapping("set")
    public void set() {
        String timeQueue = "miaosha_1001_time_queue";
        redisService.zadd(timeQueue, Double.valueOf(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30)), UUID.randomUUID().toString());

    }

    /**
     * 针对商品和用户生成动态秒杀链接
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @GetMapping("path")
    public String path(Integer userId, Integer goodsId) {

        /**
         * 忽略验证用户以及商品合法性
         */
        String key1 = miaosha + "_" + goodsId + "_path";
        String key2 = userId + "_" + goodsId;
        String v = UUID.randomUUID().toString();
        Boolean set = redisService.hset(key1, key2, v);
        if (set) {
            return v;
        } else {
            return "已经报名";
        }
    }

    /**
     * 开始秒杀
     *
     * @param userId
     * @param goodsId
     * @param path
     * @return
     */
    @GetMapping("/{path}/miaosha")
    public String path(@RequestParam("goodsId") Long userId, @RequestParam("goodsId") Long goodsId, @PathVariable("path") String path) {
        String key1 = miaosha + "_" + goodsId + "_path";
        String key2 = userId + "_" + goodsId;
        String v = redisService.hget(key1, key2);
        if (!path.equals(v)) {
            return "请先报名";
        }
        this.begin(userId, goodsId);
        return "稍等查看秒杀结果";
    }

    @GetMapping("/miaosha/pay")
    public void pay(int var1) {

    }

    /**
     * 秒杀逻辑
     */
    public void begin(Long userId, Long goodsId) {
        final CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    /*boolean start = start();
                    if (!start) {
                        log.error(Thread.currentThread().getName() + "- 秒杀还未开始");
                        return;
                    }
                    boolean end = end();
                    if (end) {
                        log.error(Thread.currentThread().getName() + "- 秒杀已结束");
                        return;
                    }
                    boolean limit = limit();
                    if (!limit) {
                        log.error(Thread.currentThread().getName() + "- 限流");
                        return;
                    }
                    boolean accepted = accepted();
                    if (!accepted) {
                        log.info(Thread.currentThread().getName() + "- 拒绝下单请求");
                        return;
                    }*/
                    boolean order = order(goodsId);
                    if (!order) {
                        log.info(Thread.currentThread().getName() + "- 下单失败");
                        return;
                    }
                    String uuid = UUID.randomUUID().toString();
                    //redisService.lPush(miaosha + "_1001_" + "success", uuid);
                    MiaoshaOrder miaoshaOrder = MiaoshaOrder.builder().userId(userId).goodsId(goodsId).orderId(uuid).status(0).createTime(new Date()).build();
                    producer.send(PlatformConstants.ORDER_QUEUE_XXX, JSONObject.toJSONString(miaoshaOrder));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        latch.countDown();
    }

    @GetMapping("reduce")
    public int reduce(Long goodsId, Integer version) {
        return goodsMapper.reduceStock(goodsId, version);
    }

    /**
     * 是否开始
     *
     * @return
     */
    private boolean start() {
        Integer start = Integer.valueOf(redisService.hget(miaosha + "_1001_" + "time", "Start"));
        return start == 1;

    }

    /**
     * 是否结束
     *
     * @return
     */
    private boolean end() {
        LocalDateTime date = LocalDateTime.now();
        String endTime = redisService.hget(miaosha + "_1001_" + "time", "End_Time");
        LocalDateTime end = LocalDateTime.parse(endTime);
        return date.isAfter(end);

    }

    /**
     * 获取下单资格
     *
     * @return
     */
    private boolean accepted() {
        Integer count = redisService.get(miaosha + "_1001_" + "count", Integer.TYPE);
        Integer access = redisService.get(miaosha + "_1001_" + "access", Integer.TYPE);

        if (access.intValue() <= count) {
            redisService.incr(miaosha + "_1001_" + "access");
            //log.info("成功请求" + Thread.currentThread().getName() + "-" + access.intValue());
            return true;
        }
        //log.info("失败请求" + Thread.currentThread().getName() + "-" + access.intValue());
        return false;
    }

    /**
     * 下单
     *
     * @return
     */
    private boolean order(Long goodsId) {
        String lua =
                "local n = tonumber(ARGV[1])" +
                        "if not n  or n == 0 then" +
                        "    return 0       " +
                        "end                " +
                        "local vals = redis.call('HMGET', KEYS[1], 'Total', 'Booked');" +
                        "local total = tonumber(vals[1])" +
                        "local blocked = tonumber(vals[2])" +
                        "if not total or not blocked then" +
                        "    return 0       " +
                        "end                " +
                        "if blocked + n <= total then" +
                        "    redis.call('HINCRBY', KEYS[1], 'Booked', n)                                   " +
                        "    return n;   " +
                        "end                " +
                        "return 0";

        String luaScript = redisService.scriptLoad(lua);

        String[] keys = new String[]{"miaosha_" + goodsId};
        String[] args = new String[]{"1"};

        Long result = redisService.evalsha(luaScript, ScriptOutputType.INTEGER, keys, args);
        System.out.println("lua result:" + result);
        return result > 0;
    }

    /**
     * 限流
     *
     * @return
     */
    private boolean limit() {
        String lua =
                "local key = KEYS[1] " +
                        " local limit = tonumber(ARGV[1]) " +
                        " local current = tonumber(redis.call('get', key) or '0')" +
                        " if current + 1 > limit " +
                        " then  return 0 " +
                        " else " +
                        " redis.call('INCRBY', key,'1')" +
                        " redis.call('expire', key,'2') " +
                        " end return 1 ";

        // 当前秒
        String key = "ip:" + System.currentTimeMillis() / 1000;
        // 最大限制
        String limit = "3";
        List<String> listKey = new ArrayList<>();
        listKey.add(key);
        List<String> listArgs = new ArrayList<>();
        listArgs.add(limit);
        String[] keys = listKey.toArray(new String[listKey.size()]);
        String[] args = listArgs.toArray(new String[listArgs.size()]);

        String luaScript = redisService.scriptLoad(lua);
        Long result = redisService.evalsha(luaScript, ScriptOutputType.INTEGER, keys, args);
        return result == 1;
    }

    /**
     * 初始化秒杀商品
     * (实际场景初始化的时候有一系列检查，秒杀时间，库存之类的，是否合格，判断有key存在则不继续操作，否则数据会被重新初始化)
     */
    @Override
    public void afterSingletonsInstantiated() {
        //模拟数据库商品数据miaosha_商品id
        /*String start = miaosha + "_1001" + "_time";
        String count = miaosha + "_1001" + "_count";
        String access = miaosha + "_1001" + "_access";

        redisService.hset(start, "Start", 0);
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime startDate = date.plusSeconds(5);
        LocalDateTime endDate = date.plusSeconds(600);

        redisService.hset(start, "Start_Time", startDate);
        redisService.hset(start, "End_Time", endDate);
        redisService.set(count, 10);
        redisService.set(access, 0);
        String totalKey = "Total";
        String bookedKey = "Booked";
        redisService.hset(miaosha + "_1001", totalKey, 10);
        redisService.hset(miaosha + "_1001", bookedKey, 0);*/
        List<MiaoshaGoods> miaoshaGoodsList = goodsMapper.listGoodsVo();
        miaoshaGoodsList.forEach(e -> {
            Long goodsId = e.getGoodsId();
            Integer stockCount = e.getStockCount();
            Date startDate = e.getStartDate();
            Date endDate = e.getEndDate();

            String start = miaosha + "_" + goodsId + "_time";
            String count = miaosha + "_" + goodsId + "_count";
            String access = miaosha + "_" + goodsId + "_access";

            redisService.hset(start, "Start_Time", startDate);
            redisService.hset(start, "End_Time", endDate);

            redisService.hset(start, "Start", 0);
            redisService.set(count, stockCount);
            redisService.set(access, 0);

            String totalKey = "Total";
            String bookedKey = "Booked";
            redisService.hset(miaosha + "_" + goodsId, totalKey, stockCount);
            redisService.hset(miaosha + "_" + goodsId, bookedKey, 0);
        });
    }
}
