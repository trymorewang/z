package com.block.queue.loadlistener;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 轮询实现类（公平）
 * <p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/15 9:57
 * @Version 1.0
 */
@Slf4j
public class RoundRobinRule implements IRule {

    /*public static void main(String[] args) {
        List<QueueListener> queueListeners = new ArrayList<>();
        QueueListener q1 = new Topic1Consumer();
        QueueListener q2 = new Topic2Consumer();
        queueListeners.add(q1);
        queueListeners.add(q2);

        RoundRobinRule r = new RoundRobinRule();
        for (int i = 0; i < 10; i++) {
            System.out.println(r.choose(queueListeners));
        }
    }*/


    private AtomicInteger nextServerCyclicCounter;

    public RoundRobinRule(AtomicInteger nextServerCyclicCounter) {
        this.nextServerCyclicCounter = nextServerCyclicCounter;
    }

    @Override
    public Object choose(List queueListeners) {
        if (queueListeners == null) {
            log.warn("no load balancer");
            return null;
        } else {
            Object listener = null;
            int count = 0;

            while (true) {
                if (listener == null && count++ < 10) {

                    int serverCount = queueListeners.size();
                    if (serverCount != 0) {
                        int nextServerIndex = this.incrementAndGetModulo(serverCount);
                        listener = queueListeners.get(nextServerIndex);
                        if (listener == null) {
                            Thread.yield();
                        } else {
                            return listener;
                        }
                        continue;
                    }

                    log.warn("No up QueueListeners available from load balancer: " + queueListeners);
                    return null;
                }

                if (count >= 10) {
                    log.warn("No available alive servers after 10 tries from load QueueListener: " + queueListeners);
                }

                return listener;
            }
        }
    }


    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while (!this.nextServerCyclicCounter.compareAndSet(current, next));

        return next;
    }


}
