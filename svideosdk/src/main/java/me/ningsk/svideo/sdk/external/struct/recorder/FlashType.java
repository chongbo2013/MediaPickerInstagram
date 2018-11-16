package me.ningsk.svideo.sdk.external.struct.recorder;

/**
 * <p>描述：闪光灯类型<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 29<br>
 * 版本：v1.0<br>
 */
public enum FlashType {
    OFF("off"),

    ON("on"),

    AUTO("auto"),

    TORCH("torch");

    private String type;

    private FlashType(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}
