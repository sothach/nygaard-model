package org.seefin.nygaard.model.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Write-once, read-many Set<br/>
 * Set implementation that only allows query methods
 * <p/>
 * Note: this collection cannot implement the java.util.Map interface, as this would
 * not be type-safe (mutating operations would be visible, yet have to throw an
 * exception)
 *
 * @param <T> Data type for Set values
 * @author phillipsr
 */
public final class WormSet<T>
        implements Iterable<T>, Serializable {
    private final Set<T> set;

    public WormSet(Collection<T> data) {
        if (data == null || data.isEmpty() == true) {
            throw new IllegalArgumentException("ReadOnlySet requires initial data");
        }
        set = Collections.unmodifiableSet(new HashSet<>(data));
    }

    public int size() {
        return set.size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public boolean contains(Object o) {
        return set.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    public Object[] toArray() {
        return set.toArray();
    }

    public <E> E[] toArray(E[] a) {
        return set.toArray(a);
    }

    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    /**
     * @return a regular (e.g., read/write) copy of this set
     */
    public Set<T>
    toSet() {
        return new HashSet<>(set);
    }

    @Override
    public String
    toString() {
        return set.toString();
    }

}
