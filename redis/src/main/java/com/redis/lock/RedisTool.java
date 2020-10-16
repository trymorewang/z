package com.redis.lock;


import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * <p>
 *  redis分布式锁工具类(单机)，集群环境可以使用Redisson实现
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/17 13:30
 * @Version 1.0
 */
public class RedisTool {

    private static final String LOCK_SUCCESS = "OK";

    private static final String SET_IF_NOT_EXIST = "NX";

    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    private static final String Key = "KEY";

    /**
     * <p>
     * 尝试获取分布式锁
     * </P>
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁(确保唯一，可使用UUID.randomUUID().toString())
     * @param requestId  请求标识
     * @param expireTime 过期时间
     * @return 是否获取成功
     *
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     *
     * <p>
     *     错误示范1
     *
     *     实现思路：使用jedis.setnx()和jedis.expire()组合实现加锁
     *
     *     问题：setnx()方法作用就是SET IF NOT EXIST，expire()方法就是给锁加一个过期时间。乍一看好像和前面的set()方法结果一样，
     *     然而由于这是两条Redis命令，不具有原子性，如果程序在执行完setnx()之后突然崩溃，导致锁没有设置过期时间。
     *     那么将会发生死锁。网上之所以有人这样实现，是因为低版本的jedis并不支持多参数的set()方法
     * </p>
     *
     * @param jedis
     * @param lockKey
     * @param requestId
     * @param expireTime
     */
    public static void wrongGetLock1(Jedis jedis, String lockKey, String requestId, int expireTime) {

        Long result = jedis.setnx(lockKey, requestId);

        if (result == 1) {

            // 若在这里程序突然崩溃，则无法设置过期时间，将发生死锁
            jedis.expire(lockKey, expireTime);
        }
    }

    /**
     *
     * <p>
     *     错误示范2
     *
     *     实现思路：使用jedis.setnx()命令实现加锁，其中key是锁，value是锁的过期时间。
     *     执行过程：
     *     1. 通过setnx()方法尝试加锁，如果当前锁不存在，返回加锁成功。
     *     2. 如果锁已经存在则获取锁的过期时间，和当前时间比较，如果锁已经过期，则设置新的过期时间，返回加锁成功
     *
     *      问题：
     *     1. 由于是客户端自己生成过期时间，所以需要强制要求分布式下每个客户端的时间必须同步。
     *     2. 当锁过期的时候，如果多个客户端同时执行jedis.getSet()方法，那么虽然最终只有一个客户端可以加锁，但是这个客户端的锁的过期时间可能被其他客户端覆盖。
     *     3. 锁不具备拥有者标识，即任何客户端都可以解锁。
     * </p>
     *
     * @param jedis
     * @param lockKey
     * @param expireTime
     * @return
     */
    public static boolean wrongGetLock2(Jedis jedis, String lockKey, int expireTime) {

        long expires = System.currentTimeMillis() + expireTime;
        String expiresStr = String.valueOf(expires);

        // 如果当前锁不存在，返回加锁成功
        if (jedis.setnx(lockKey, expiresStr) == 1) {
            return true;
        }

        // 如果锁存在，获取锁的过期时间
        String currentValueStr = jedis.get(lockKey);
        if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
            String oldValueStr = jedis.getSet(lockKey, expiresStr);
            if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {

                // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才有权利加锁
                return true;
            }
        }
        // 其他情况，一律返回加锁失败
        return false;
    }

    /**
     * <P>
     *     释放分布式锁
     *
     *     实现思路：Lua脚本保证执行原子性，KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。eval()方法是将Lua代码交给Redis服务端执行。
     *     Lua脚本含义：获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）
     * </P>
     *
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */

    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     *
     * <p>
     *     错误示范1
     *
     *     问题：不判断锁的拥有者直接解锁，任何客户端都可以解锁，即使这把锁不是他的。
     * </p>
     *
     * @param jedis
     * @param lockKey
     */
    public static void wrongReleaseLock1(Jedis jedis, String lockKey) {
        jedis.del(lockKey);
    }

    /**
     * <p>
     *      错误示范2
     *
     *      问题：客户端A加锁，一段时间之后客户端A解锁，在执行jedis.del()之前(requestId.equals(jedis.get(lockKey) 判断成立)，业务逻辑执行超时，锁过期了，
     *      此时客户端B尝试加锁成功，然后客户端A再执行del()方法，则将客户端B的锁给解除了
     * </p>
     *
     * @param jedis
     * @param lockKey
     * @param requestId
     */
    public static void wrongReleaseLock2(Jedis jedis, String lockKey, String requestId) {
        // 判断加锁与解锁是不是同一个客户端
        if (requestId.equals(jedis.get(lockKey))) {
            // 若在此时，这把锁突然不是这个客户端的，则会误解锁
            jedis.del(lockKey);
        }
    }


    public static void main(String[] args) {
        ExecutorService executorService = newFixedThreadPool(10);
        //引入countDownLatch进行线程同步
        CountDownLatch cdt = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                Jedis jedis = new Jedis("localhost", 6379);
                test(jedis);
                jedis.close();
                cdt.countDown();
            });
        }
        try {
            cdt.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public static void test(Jedis jedis) {
        String requestId = UUID.randomUUID().toString();
        String lockKey = "KL";
        int expireTime = 5000;
        boolean isLock = tryGetDistributedLock(jedis, lockKey, requestId, expireTime);
        if (isLock) {
            System.out.println(requestId + "：获取锁");
            System.out.println("执行业务完成");
            releaseDistributedLock(jedis, lockKey, requestId);
        } else {
            System.out.println(requestId + "- 等待锁");
        }
    }
}
