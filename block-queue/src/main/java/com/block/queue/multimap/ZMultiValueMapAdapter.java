package com.block.queue.multimap;

import com.block.queue.loadlistener.IRule;
import com.block.queue.loadlistener.RoundRobinRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/14 15:59
 * @Version 1.0
 */
@Slf4j
public class ZMultiValueMapAdapter<K, V> implements ZMultiValueMap<K, V>, Serializable {


    private final Map<K, List<V>> targetMap;
    private AtomicInteger nextServerCyclicCounter;
    private IRule rule;

    ZMultiValueMapAdapter(Map<K, List<V>> targetMap) {
        this.targetMap = targetMap;
        this.nextServerCyclicCounter = new AtomicInteger(0);
        this.rule = new RoundRobinRule(nextServerCyclicCounter);
    }


    @Nullable
    public V getFirst(K key) {
        List<V> values = (List) this.targetMap.get(key);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public void add(K key, @Nullable V value) {
        List<V> values = (List) this.targetMap.computeIfAbsent(key, (k) -> {
            return new LinkedList();
        });
        values.add(value);
    }

    public void addAll(K key, List<? extends V> values) {
        List<V> currentValues = (List) this.targetMap.computeIfAbsent(key, (k) -> {
            return new LinkedList();
        });
        currentValues.addAll(values);
    }

    public void addAll(MultiValueMap<K, V> values) {
        Iterator var2 = values.entrySet().iterator();

        while (var2.hasNext()) {
            Entry<K, List<V>> entry = (Entry) var2.next();
            this.addAll(entry.getKey(), (List) entry.getValue());
        }

    }

    public void set(K key, @Nullable V value) {
        List<V> values = new LinkedList();
        values.add(value);
        this.targetMap.put(key, values);
    }

    public void setAll(Map<K, V> values) {
        values.forEach(this::set);
    }

    public Map<K, V> toSingleValueMap() {
        Map<K, V> singleValueMap = new LinkedHashMap(this.targetMap.size());
        this.targetMap.forEach((key, values) -> {
            if (values != null && !values.isEmpty()) {
                singleValueMap.put(key, values.get(0));
            }

        });
        return singleValueMap;
    }


    /**
     * <p>
     * 轮询算法获取其中一个消费者
     * </p>
     *
     * @param key
     * @return
     */
    @Nullable
    public V poll(K key) {
        List<V> values = this.targetMap.get(key);

        //最简单实现方式
       /*if (nextServerCyclicCounter.get() == values.size()) {
            nextServerCyclicCounter.set(0);
        }
        int pos = nextServerCyclicCounter.getAndIncrement();
        return values.get(pos);*/
        return (V) rule.choose(values);
    }

    /*class RoundRobinRule implements IRule {

        @Override
        public Object choose(List queueListeners) {
            if (queueListeners == null) {
                log.warn("no load balancer");
                return null;
            } else {
                Object server = null;
                int count = 0;

                while (true) {
                    if (server == null && count++ < 10) {

                        int serverCount = queueListeners.size();
                        if (serverCount != 0) {
                            int nextServerIndex = this.incrementAndGetModulo(serverCount);
                            server = queueListeners.get(nextServerIndex);
                            if (server == null) {
                                Thread.yield();
                            } else {
                                return server;
                            }
                            continue;
                        }

                        log.warn("No up QueueListeners available from load balancer: " + queueListeners);
                        return null;
                    }

                    if (count >= 10) {
                        log.warn("No available alive servers after 10 tries from load QueueListener: " + queueListeners);
                    }

                    return server;
                }
            }
        }


        private int incrementAndGetModulo(int modulo) {
            int current;
            int next;
            do {
                current = nextServerCyclicCounter.get();
                next = (current + 1) % modulo;
            } while (!nextServerCyclicCounter.compareAndSet(current, next));

            return next;
        }
    }*/

    public int size() {
        return this.targetMap.size();
    }

    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.targetMap.containsValue(value);
    }

    @Nullable
    public List<V> get(Object key) {
        return (List) this.targetMap.get(key);
    }

    @Nullable
    public List<V> put(K key, List<V> value) {
        return (List) this.targetMap.put(key, value);
    }

    @Nullable
    public List<V> remove(Object key) {
        return (List) this.targetMap.remove(key);
    }

    public void putAll(Map<? extends K, ? extends List<V>> map) {
        this.targetMap.putAll(map);
    }

    public void clear() {
        this.targetMap.clear();
    }

    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    public Collection<List<V>> values() {
        return this.targetMap.values();
    }

    public Set<Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }

    public boolean equals(@Nullable Object other) {
        return this == other || this.targetMap.equals(other);
    }

    public int hashCode() {
        return this.targetMap.hashCode();
    }

    public String toString() {
        return this.targetMap.toString();
    }
}
