package me.ningsk.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

public abstract class WeakReferenceHandler<T> extends Handler
{
    private WeakReference<T> mReference;

    public WeakReferenceHandler(Looper looper, T reference)
    {
        super(looper);
        this.mReference = new WeakReference(reference);
    }

    public void handleMessage(Message msg)
    {
        T reference = this.mReference.get();
        if (reference == null)
            return;
        handleMessage(reference, msg);
    }

    protected abstract void handleMessage(T paramT, Message paramMessage);
}
