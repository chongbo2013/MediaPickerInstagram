package me.ningsk.svideo.sdk.external.struct.common;

/**
 * <p>描述：视频质量<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 10<br>
 * 版本：v1.0<br>
 */
public enum  VideoQuality {
    SSD,
    HD,
    SD,
    LD,
    PD,
    EPD;

    private VideoQuality() {
        throw new IllegalStateException("u can't instantiate me");
    }
}
