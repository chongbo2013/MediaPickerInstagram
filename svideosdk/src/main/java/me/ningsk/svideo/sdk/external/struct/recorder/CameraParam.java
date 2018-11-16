package me.ningsk.svideo.sdk.external.struct.recorder;

/**
 * <p>描述：相机参数<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 36<br>
 * 版本：v1.0<br>
 */
public class CameraParam {
    private String flashType;
    private float zoomRatio;
    private int focusMode = 0;
    private float exposureCompensation = 0.5F;
    public static final int FOCUS_MODE_AUTO = 1;
    public static final int FOCUS_MODE_CONTINUE = 0;

    public CameraParam() {

    }

    public String getFlashType() {
        return this.flashType;
    }

    public void setFlashType(String type) {
        this.flashType = this.flashType;
    }

    public float getZoomRatio() {
        return this.zoomRatio;
    }

    public void setZoomRatio(float zoomRatio) {
        this.zoomRatio = zoomRatio;
    }

    public int getFocusMode() {
        return this.focusMode;
    }

    public void setFocusMode(int focusMode) {
        this.focusMode = focusMode;
    }

    public float getExposureCompensationRatio() {
        return this.exposureCompensation;
    }

    public void setExposureCompensationRatio(float exposureCompensation) {
        this.exposureCompensation = exposureCompensation;
    }

}
