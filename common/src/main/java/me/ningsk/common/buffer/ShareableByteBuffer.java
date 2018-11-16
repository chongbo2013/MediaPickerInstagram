package me.ningsk.common.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import me.ningsk.common.ref.Releasable;

public class ShareableByteBuffer extends AtomicShareable<ShareableByteBuffer>
        implements ByteArrayHolder, Releasable
{
    private final ByteBuffer _Data;

    public ShareableByteBuffer(Recycler<ShareableByteBuffer> recycler, int size, boolean direct)
    {
        super(recycler);
        ByteBuffer data;
        if (direct)
            data = ByteBuffer.allocateDirect(size);
        else {
            data = ByteBuffer.allocate(size);
        }

        data.position(0);
        data.limit(0);

        data.order(ByteOrder.nativeOrder());

        this._Data = data;
    }

    public ByteBuffer getData()
    {
        return this._Data;
    }

    protected void onLastRef()
    {
        this._Recycler.recycle(this);
    }

    public byte[] getByteArray()
    {
        return this._Data.array();
    }
}
