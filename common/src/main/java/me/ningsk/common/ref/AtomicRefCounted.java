package me.ningsk.common.ref;

import java.util.concurrent.atomic.AtomicInteger;

import me.ningsk.common.utils.Assert;

public abstract class AtomicRefCounted
        implements Releasable
{
    private final AtomicInteger _RefCount = new AtomicInteger();

    public AtomicRefCounted()
    {
        this._RefCount.set(1);
    }

    public void reset()
    {
        Assert.assertEquals(0, this._RefCount.get());
        this._RefCount.set(1);
    }

    public final void ref()
    {
        int rv = this._RefCount.incrementAndGet();
        Assert.assertGreaterThan(rv, 1);
    }

    protected abstract void onLastRef();

    public final void release()
    {
        int rv = this._RefCount.decrementAndGet();

        if (rv == 0) {
            onLastRef();
            return;
        }

        Assert.assertGreaterThan(rv, 0);
    }
}
