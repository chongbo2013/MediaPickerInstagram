package me.ningsk.common.media;

import me.ningsk.common.buffer.Allocator;
import me.ningsk.common.buffer.Recycler;
import me.ningsk.common.media.ShareableBitmap;

public class BitmapAllocator
        implements Allocator<ShareableBitmap>
{
    private final int _Width;
    private final int _Height;

    public BitmapAllocator(int w, int h)
    {
        this._Width = w;
        this._Height = h;
    }

    public ShareableBitmap allocate(Recycler<ShareableBitmap> recycler, ShareableBitmap reused)
    {
        if (reused != null) {
            reused.reset();
            return reused;
        }

        return new ShareableBitmap(recycler, this._Width, this._Height);
    }

    public void recycle(ShareableBitmap object)
    {
        object.setDataUsed(false);
    }

    public void release(ShareableBitmap object)
    {
        object.getData().recycle();
    }
}