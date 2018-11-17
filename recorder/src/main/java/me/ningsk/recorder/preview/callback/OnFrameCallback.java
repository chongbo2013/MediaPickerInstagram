package me.ningsk.recorder.preview.callback;


import android.hardware.Camera;

import java.util.List;

public interface OnFrameCallback {
    void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info);

    Camera.Size onChoosePreviewSize(List<Camera.Size> supportedPreviewSizes, Camera.Size preferredPreviewSizeForVideo);

    void onFailed();
}
