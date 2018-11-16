package me.ningsk.common.media;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import me.ningsk.common.buffer.Allocator;
import me.ningsk.common.buffer.Recycler;
import me.ningsk.common.ref.Releasable;

public class ThumbnailPool<T, K>
        implements Recycler<T>, Releasable
{
    private final Map<K, T> cache = Collections.synchronizedMap(new TreeMap());


    private final Allocator<T> mAllocator;
    private final int mLimit;
    private int countWhenFirstRecycle;
    private Set<K> currentShow = Collections.synchronizedSet(new TreeSet());

    public ThumbnailPool(Allocator<T> allocator, int limit) {
        this.mAllocator = allocator;
        this.mLimit = limit;
    }

    public ThumbnailPool(Allocator<T> allocator)
    {
        this(allocator, -1);
    }

    public T allocate(K key) {
        Object item = this.cache.get(key);
        if (item == null) {
            item = generateItem();
            this.cache.put(key, (T) item);
        }
        this.currentShow.add(key);
        return (T) item;
    }

    private T generateItem() {
        Object item = null;
        if (isOutOfLimit()) {
            item = findIdleItem();
            if (item == null)
                item = this.mAllocator.allocate(this, null);
            else
                this.mAllocator.recycle((T) item);
        }
        else
        {
            item = this.mAllocator.allocate(this, null);
        }
        return (T) item;
    }

    private T findIdleItem() {
        List list = null;
        synchronized (this.currentShow) {
            list = getKeyList(this.currentShow);
        }
        if (list.size() == 0) {
            return null;
        }
        Object s = list.get(0);
        Object e = list.get(list.size() - 1);
        if (this.cache.size() == 0) {
            return null;
        }

        List array = null;
        synchronized (this.cache) {
            array = getKeyList(this.cache.keySet());
        }
        int sp = array.indexOf(s);
        int ep = array.indexOf(e);
        int size = array.size();
        int mid = sp + (ep - sp) / 2;
        Object idle;
        if (mid > size - mid)
            idle = array.get(0);
        else {
            idle = array.get(size - 1);
        }
        return this.cache.remove(idle);
    }

    private List<K> getKeyList(Set<K> sk) {
        List keys = new ArrayList();
        for (Iterator localIterator = sk.iterator(); localIterator.hasNext(); ) { Object k = localIterator.next();
            keys.add(k);
        }
        return keys;
    }

    public void release()
    {
        Iterator localIterator;
        synchronized (this.cache) {
            for (localIterator = this.cache.values().iterator(); localIterator.hasNext(); ) { Object item = localIterator.next();
                this.mAllocator.release((T) item); }
        }
    }

    private boolean isOutOfLimit()
    {
        if (this.countWhenFirstRecycle == 0) {
            return false;
        }
        if (this.countWhenFirstRecycle > this.mLimit) {
            return this.countWhenFirstRecycle <= this.cache.size();
        }
        return this.mLimit <= this.cache.size();
    }

    public synchronized void recycle(T object)
    {
        if (this.countWhenFirstRecycle == 0) {
            this.countWhenFirstRecycle = this.currentShow.size();
        }

        this.mAllocator.allocate(this, object);
        Object k = findKeyByValue(object);
        if (k == null)
            this.mAllocator.release(object);
        else
            removeCurrentShowItem((K) k);
    }

    private boolean removeCurrentShowItem(K key)
    {
        return this.currentShow.remove(key);
    }

    private synchronized K findKeyByValue(T value) {
        Object key = null;
        synchronized (this.cache) {
            for (Map.Entry entry : this.cache.entrySet()) {
                Object v = entry.getValue();
                if (v == value) {
                    key = entry.getKey();
                }
            }
        }
        return (K) key;
    }
}
