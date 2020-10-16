package com.block.queue.multimap;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/09/14 15:55
 * @Version 1.0
 */
public class ZLinkedMultiValueMap<K, V> extends ZMultiValueMapAdapter<K, V> implements Serializable, Cloneable {
    private static final long serialVersionUID = 3801124242820219131L;

    public ZLinkedMultiValueMap() {
        super(new LinkedHashMap());
    }

    public ZLinkedMultiValueMap(int initialCapacity) {
        super(new LinkedHashMap(initialCapacity));
    }

    public ZLinkedMultiValueMap(Map<K, List<V>> otherMap) {
        super(new LinkedHashMap(otherMap));
    }

    public ZLinkedMultiValueMap<K, V> deepCopy() {
        ZLinkedMultiValueMap<K, V> copy = new ZLinkedMultiValueMap(this.size());
        this.forEach((key, values) -> {
            copy.put(key, new LinkedList(values));
        });
        return copy;
    }

    public ZLinkedMultiValueMap<K, V> clone() {
        return new ZLinkedMultiValueMap(this);
    }
}
