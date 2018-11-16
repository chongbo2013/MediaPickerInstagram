package me.ningsk.common.media;


import me.ningsk.common.buffer.Allocator;
import me.ningsk.common.buffer.Recycler;

public class SharedBitmapAllocator implements Allocator<ShareableBitmap> {
    private final int mWidth;
    private final int mHeight;

    public SharedBitmapAllocator(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
    }

    public ShareableBitmap allocate(Recycler<ShareableBitmap> recycler, ShareableBitmap reused) {
        if (reused != null) {
            reused.reset();
            return reused;
        } else {
            return new ShareableBitmap(recycler, this.mWidth, this.mHeight);
        }
    }

    public void recycle(ShareableBitmap object) {
        object.setDataUsed(false);
    }

    public void release(ShareableBitmap object) {
        object.getData().recycle();
    }
}

