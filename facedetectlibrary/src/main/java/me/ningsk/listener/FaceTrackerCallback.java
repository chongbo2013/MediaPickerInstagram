package me.ningsk.listener;

/**
 * <p>描述：人脸关键点检测回调<p>
 * 作者：ningsk<br>
 * 日期：2018/10/31 08 32<br>
 * 版本：v1.0<br>
 */
public interface FaceTrackerCallback {
    /**
     * 检测完成回调
     */
    void onTrackingFinish();
}
