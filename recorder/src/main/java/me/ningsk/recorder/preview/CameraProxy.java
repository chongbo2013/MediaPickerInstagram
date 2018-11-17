package me.ningsk.recorder.preview;


import android.hardware.Camera;

import me.ningsk.svideo.sdk.external.struct.recorder.CameraType;

public class CameraProxy implements Camera.PreviewCallback{
    private static final String TAG = CameraProxy.class.getSimpleName();
    private static final int CAMERA_FRONT = 1;
    private static final int CAMERA_BACK = 0;
    public static final int BASE_CAMERA_ORIENTATION = 270;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    public interface OnFrameCallBackInternal {
       void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info) ;
    }
}
