package me.ningsk.log.core;


import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class LogService
{
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    public LogService(String serviceName)
    {
        this.mHandlerThread = new HandlerThread(serviceName);
        this.mHandlerThread.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
    }

    public void execute(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public void quit() {
        if (this.mHandlerThread != null)
            if (Build.VERSION.SDK_INT >= 18)
                this.mHandlerThread.quitSafely();
            else
                this.mHandlerThread.quit();
    }

    private class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private LogUncaughtExceptionHandler() {
        }

        public void uncaughtException(Thread t, Throwable e) {
            Log.e("JrLogger", "Capture log info failed!", e);
        }
    }
}
