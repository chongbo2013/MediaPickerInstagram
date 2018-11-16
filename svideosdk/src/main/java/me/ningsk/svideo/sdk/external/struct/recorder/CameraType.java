package me.ningsk.svideo.sdk.external.struct.recorder;

/**
 * <p>描述：前后置摄像头<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 24<br>
 * 版本：v1.0<br>
 */
public enum  CameraType {
    FRONT(1),
    BACK(2);

    private int type;

    private CameraType(int type) {
        this.type = type;
    }

    private int getType() {
        return this.type;
    }
}
