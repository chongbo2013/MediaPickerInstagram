package me.ningsk.svideo.sdk.external.struct.encoder;

/**
 * <p>描述：视频编解码信息<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 17<br>
 * 版本：v1.0<br>
 */
public enum  VideoCodecs {
    H264_HARDWARE,

    H264_SOFT_OPENH264,

    H264_SOFT_FFMPEG;

    private VideoCodecs() {
        throw new IllegalStateException("u can't instantiate me");
    }

    public static VideoCodecs getInstanceByValue(int v) {
        switch (v) {
            case 0:
                return H264_HARDWARE;
            case 1:
                return H264_SOFT_OPENH264;
            case 2:
                return H264_SOFT_FFMPEG;
            default:
                return H264_HARDWARE;
        }
    }

}
