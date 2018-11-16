package me.ningsk.common.buffer;

import java.util.ArrayList;
import java.util.Iterator;

import me.ningsk.common.ref.Releasable;

public class Pool<T>
        implements Recycler<T>, Releasable
{
    private final Allocator<T> _Allocator;
    private final ArrayList<T> _Cache = new ArrayList();

    public Pool(Allocator<T> alloc)
    {
        this._Allocator = alloc;
    }

    public Allocator<T> getAllocator()
    {
        return this._Allocator;
    }

    public T allocate()
    {
        Object item = this._Cache.isEmpty() ? null : this._Cache.remove(this._Cache.size() - 1);

        return this._Allocator.allocate(this, (T)item);
    }

    public void recycle(T object)
    {
        this._Cache.add(object);
    }

    public void release()
    {
        Iterator localIterator = this._Cache.iterator();
        while ( localIterator.hasNext()) {
            Object item = localIterator.next();
            this._Allocator.release((T)item);
        }
    }
}
