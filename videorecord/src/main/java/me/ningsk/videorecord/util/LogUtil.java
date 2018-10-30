package me.ningsk.videorecord.util;

import android.util.Log;

/**
 * <p>描述：日志工具类<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 39<br>
 * 版本：v1.0<br>
 */
public class LogUtil {
    public static String TAG = Config.TAG;
    public static final boolean LOG_ENABLE = Config.DEBUG;

    public static void logd(String msg) {
        if (LOG_ENABLE) Log.d(TAG, msg);
    }

    public static void logd(String tag, String msg) {
        if (LOG_ENABLE) Log.d(TAG + "-" + tag, msg);
    }

    public static void loge(String msg) {
        Log.e(TAG, msg);
    }

    public static void loge(String tag, String msg) {
        Log.e(TAG + "-" + tag, msg);
    }

    public static void logi(String msg) {
        Log.i(TAG, msg);
    }

    public static void logi(String tag, String msg) {
        Log.i(TAG + "-" + tag, msg);
    }

    public static void logw(String msg) {
        Log.w(TAG, msg);
    }

    public static void logw(String tag, String msg) {
        Log.w(TAG + "-" + tag, msg);
    }

    public static void logv(String msg) {
        Log.v(TAG, msg);
    }

    public static void logv(String tag, String msg) {
        Log.v(TAG + "-" + tag, msg);
    }

}

