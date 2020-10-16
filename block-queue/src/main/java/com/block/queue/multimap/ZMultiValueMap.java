package com.block.queue.multimap;

import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/14 16:07
 * @Version 1.0
 */
public interface ZMultiValueMap<K, V> extends Map<K, List<V>> {
    @Nullable
    V getFirst(K var1);

    void add(K var1, @Nullable V var2);

    void addAll(K var1, List<? extends V> var2);

    void addAll(org.springframework.util.MultiValueMap<K, V> var1);

    default void addIfAbsent(K key, @Nullable V value) {
        if (!this.containsKey(key)) {
            this.add(key, value);
        }

    }

    void set(K var1, @Nullable V var2);

    void setAll(Map<K, V> var1);

    Map<K, V> toSingleValueMap();

    @Nullable
    V poll(K var1);
}
