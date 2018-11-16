package me.ningsk.common.ref;

import me.ningsk.common.utils.Assert;

public abstract class RefCounted
        implements Releasable
{
    private int _RefCount;

    public RefCounted()
    {
        this._RefCount = 1;
    }

    public void reset()
    {
        Assert.assertEquals(0, this._RefCount);
        this._RefCount = 1;
    }

    public final void ref()
    {
        Assert.assertGreaterThan(this._RefCount, 0);
        this._RefCount += 1;
    }

    protected abstract void onLastRef();

    public final void release()
    {
        this._RefCount -= 1;

        if (this._RefCount == 0) {
            onLastRef();
            return;
        }

        Assert.assertGreaterThan(this._RefCount, 0);
    }
}