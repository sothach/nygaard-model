package org.seefin.nygaard.model.collections;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Write-once, read-many Map<br/>
 * Map implementation that only allows query methods, and ensures that methods
 * such as keySet() and values() return collections that themselves do not allow
 * modification
 * <p/>
 * Note: this collection cannot implement the java.util.Map interface, as this would
 * not be type-safe (mutating operations would be visible, yet have to throw an
 * exception)
 *
 * @param <K> Data type for Map keys
 * @param <V> Data type for Map values
 * @author phillipsr
 */
public final class WormMap<K, V>
        implements Serializable {
    private final Map<K, V> map;

    public WormMap(Map<K, V> data) {
        if (data == null) {
            throw new IllegalArgumentException("ReadOnlyMap requires initial data");
        }
        map = Collections.unmodifiableMap(data);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public V get(Object key) {
        return map.get(key);
    }

    public WormSet<K> keySet() {
        return new WormSet<>(map.keySet());
    }

    public WormList<V> values() {
        return new WormList<>(map.values());
    }

    public WormSet<java.util.Map.Entry<K, V>> entrySet() {
        return new WormSet<>(map.entrySet());
    }

    /**
     * @return a regular (e.g., read/write) copy of this map
     */
    public Map<K, V>
    toMap() {
        return new HashMap<>(map);
    }

    @Override
    public String
    toString() {
        return map.toString();
    }

}
