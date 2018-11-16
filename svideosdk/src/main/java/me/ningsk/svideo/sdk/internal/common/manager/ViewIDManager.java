package me.ningsk.svideo.sdk.internal.common.manager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 17 34<br>
 * 版本：v1.0<br>
 */
public class ViewIDManager {
    private AtomicInteger mValue;
    private int mInitialValue;

    public ViewIDManager(int initialValue) {
        this.mValue = new AtomicInteger(initialValue);
    }

    public synchronized int incrementAndGet() {
        return mValue.incrementAndGet();
    }

    public synchronized int decrementAndGet() {
        return mValue.decrementAndGet();
    }

    public void resetInitialValue() {
        mValue.set(mInitialValue);
    }
}
