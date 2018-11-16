package me.ningsk.common.buffer;


import java.util.ArrayList;
import java.util.Iterator;

import me.ningsk.common.ref.Releasable;

public class SynchronizedPool<T>
        implements Recycler<T>, Releasable
{
    private final Allocator<T> _Allocator;
    private final OnBufferAvailableListener _OnBufferAvailableListener;
    private int _Limit;
    private final ArrayList<T> _Cache = new ArrayList();

    public SynchronizedPool(Allocator<T> alloc, OnBufferAvailableListener listener, int limit)
    {
        this._Allocator = alloc;
        this._OnBufferAvailableListener = listener;
        this._Limit = limit;
    }

    public SynchronizedPool(Allocator<T> alloc)
    {
        this(alloc, null, -1);
    }

    public Allocator<T> getAllocator() {
        return this._Allocator;
    }

    public synchronized T allocate()
    {
        if (!this._Cache.isEmpty()) {
            Object item = this._Cache.remove(this._Cache.size() - 1);
            return this._Allocator.allocate(this, (T)item);
        }

        if (this._Limit == 0) {
            return null;
        }

        if (this._Limit > 0) {
            this._Limit -= 1;
        }

        return this._Allocator.allocate(this, null);
    }

    public void recycle(T object)
    {
        boolean notify_available;
        synchronized (this) {
            notify_available = (this._Limit == 0) && (this._Cache.isEmpty());
            this._Cache.add(object);
        }
        if ((notify_available) && (this._OnBufferAvailableListener != null))
            this._OnBufferAvailableListener.onBufferAvailable(this);
    }

    public synchronized void release()
    {
        Iterator localIterator = this._Cache.iterator();
        while ( localIterator.hasNext() ) {
            Object item = localIterator.next();
            this._Allocator.release((T) item);
        }
    }

    public static abstract interface OnBufferAvailableListener
    {
        public abstract void onBufferAvailable(SynchronizedPool<?> paramSynchronizedPool);
    }
}
