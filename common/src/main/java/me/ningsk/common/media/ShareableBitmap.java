package me.ningsk.common.media;

import android.graphics.Bitmap;

import me.ningsk.common.buffer.AtomicShareable;
import me.ningsk.common.buffer.Recycler;

public class ShareableBitmap extends AtomicShareable<ShareableBitmap>
{
    private final Bitmap _Data;
    private boolean isDataUsed;

    public ShareableBitmap(Recycler<ShareableBitmap> recycler, int w, int h)
    {
        super(recycler);
        this._Data = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
    }

    public ShareableBitmap(Bitmap bitmap) {
        super(null);

        this._Data = bitmap;
    }

    public boolean isDataUsed() {
        return this.isDataUsed;
    }

    public void setDataUsed(boolean dataUsed) {
        this.isDataUsed = dataUsed;
    }

    protected void onLastRef()
    {
        if (this._Recycler != null) {
            this._Recycler.recycle(this);
        }
        else if (!this._Data.isRecycled())
            this._Data.recycle();
    }

    public Bitmap getData()
    {
        this.isDataUsed = true;
        return this._Data;
    }
}
