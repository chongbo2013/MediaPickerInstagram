package me.ningsk.videotracklibrary.utils;

import android.content.Context;

import me.ningsk.utilslibrary.utils.DeviceUtils;
import me.ningsk.utilslibrary.utils.UnitConverter;
import me.ningsk.videotracklibrary.interfaces.VideoTrimListener;

/**
 * <p>描述：视频帧处理<p>
 * 作者：ningsk<br>
 * 日期：2018/11/13 16 36<br>
 * 版本：v1.0<br>
 */
public class VideoTrimmerUtil {

    private static final String TAG = VideoTrimmerUtil.class.getSimpleName();
    public static final long MIN_SHOOT_DURATION = 3000L; // 最小剪辑时间3s
    public static final int VIDEO_MAX_TIME = 10; // 10秒
    public static final long MAX_SHOOT_DURATION = VIDEO_MAX_TIME * 1000L; // 视频最多裁剪多长时间10s
    public static final int MAX_COUNT_RANGE = 10; // seekbar的区域内一共有多少照片
    public static final int SCREEN_WIDTH_FULL = DeviceUtils.getDeviceWidth();
    public static final int RECYCLER_VIEW_PADDING = UnitConverter.dpToPx(35);
    public static final int VIDEO_FRAMES_WIDTH = SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2;
    private static final int THUMB_WIDTH = (SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2) / VIDEO_MAX_TIME;
    private static final int THUMB_HEIGHT = UnitConverter.dpToPx(50);

    public static void trim(Context context, String inputFile, String outputFile, long startMs, long endMs, final VideoTrimListener listener) {

    }
}
