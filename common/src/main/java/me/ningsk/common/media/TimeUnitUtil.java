package me.ningsk.common.media;

import java.util.concurrent.TimeUnit;

/**
 * <p>描述：时间单位工具类<p>
 * 作者：ningsk<br>
 * 日期：2018/11/16 10 12<br>
 * 版本：v1.0<br>
 */
public class TimeUnitUtil {
    public TimeUnitUtil() {
    }

    private static float fromNanos(long value, TimeUnit tu) {
        return (float)value / (float)tu.toNanos(1L);
    }

    private static long toNanos(float value, TimeUnit tu) {
        return (long)((float)tu.toNanos(1L) * value);
    }

    private static long toMillis(float value, TimeUnit tu) {
        return (long)((float)tu.toMillis(1L) * value);
    }

    public static long secondsToNanos(float value) {
        return toNanos(value, TimeUnit.SECONDS);
    }

    public static float nanosToSeconds(long value) {
        return fromNanos(value, TimeUnit.SECONDS);
    }

    public static long millisecondsToNanos(float value) {
        return toNanos(value, TimeUnit.MILLISECONDS);
    }

    public static float nanosToMilliseconds(long value) {
        return fromNanos(value, TimeUnit.MILLISECONDS);
    }

    public static long secondsToMillis(float time) {
        return toMillis(time, TimeUnit.SECONDS);
    }
}

