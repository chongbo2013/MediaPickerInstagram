package me.ningsk.common.buffer;

public abstract interface Allocator<T>
{
    public abstract T allocate(Recycler<T> recycler, T t);

    public abstract void recycle(T t);

    public abstract void release(T t);
}
