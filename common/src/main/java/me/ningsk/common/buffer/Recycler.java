package me.ningsk.common.buffer;


public interface Recycler<T> {
    void recycle(T t);
}
