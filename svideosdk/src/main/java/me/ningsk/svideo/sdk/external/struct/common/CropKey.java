package me.ningsk.svideo.sdk.external.struct.common;

/**
 * <p>描述：裁剪参数常量<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 07<br>
 * 版本：v1.0<br>
 */
public class CropKey {
    public static final String VIDEO_PATH = "video_path";
    public static final String VIDEO_DURATION = "video_duration";
    public static final String VIDEO_RESOLUTION = "video_resolution";
    public static final String VIDEO_SCALE = "crop_mode";
    public static final String VIDEO_QUALITY = "video_quality";
    public static final String VIDEO_FRAMERATE = "video_framerate";
    public static final String VIDEO_RATIO = "video_ratio";
    public static final String VIDEO_GOP = "video_gop";
    public static final String VIDEO_BITRATE = "video_bitrate";
    public static final VideoDisplayMode SCALE_CROP = VideoDisplayMode.SCALE;
    public static final VideoDisplayMode SCALE_FILL = VideoDisplayMode.FILL;
    public static final String RESULT_KEY_CROP_PATH = "crop_path";
    public static final String RESULT_KEY_FILE_PATH = "file_path";
    public static final String RESULT_KEY_DURATION = "duration";
    public static final String RESULT_KEY_START_TIME = "start_time";
    public static final String ACTION = "action";
    public static final int RATIO_MODE_1_1 = 1;
    public static final int RATIO_MODE_3_4 = 0;
    public static final int RATIO_MODE_9_16 = 2;
    public static final int RESOLUTION_360P = 0;
    public static final int RESOLUTION_480P = 1;
    public static final int RESOLUTION_540P = 2;
    public static final int RESOLUTION_720P = 3;
}
