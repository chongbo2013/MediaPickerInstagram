package me.ningsk.cameralibrary.engine.listener;

/**
 * <p>描述：相机回调<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 35<br>
 * 版本：v1.0<br>
 */
public interface OnCameraCallback {

    // 相机已打开
    void onCameraOpened();

    // 预览回调
    void onPreviewCallback(byte[] data);
}
