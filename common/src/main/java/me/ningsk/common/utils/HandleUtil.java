package me.ningsk.common.utils;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HandleUtil {
    public static void quitSafely(Handler handler) {
        Looper looper = handler.getLooper();
        handler.post(()->{
            looper.quit();
        });
    }

    public static void quitSafely(HandlerThread thread) {
        quitSafely(new Handler(thread.getLooper()));
    }
}
