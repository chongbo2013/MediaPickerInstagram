package me.ningsk.recorder.camera;


import android.graphics.Point;
import android.opengl.GLSurfaceView;

import me.ningsk.recorder.preview.CameraProxy;
import me.ningsk.recorder.preview.callback.OnFrameCallback;
import me.ningsk.svideo.sdk.external.struct.recorder.CameraParam;
import me.ningsk.svideo.sdk.external.struct.recorder.FlashType;

public interface JRICamera {
    void setDisplayView(GLSurfaceView glSurfaceView);

    void setCameraId(int cameraId);

    void setPreviewSize(int width, int height, boolean isAutoAdjust);

    void startPreview();

    void switchCamera();

    FlashType switchLight();

    void setLight(FlashType type);

    void setZoom(int rate);

    int getCameraCount();

    int switchCamera(CameraParam cameraParam);

    void setCameraParam(CameraParam cameraParam);

    void setFocus(Point point);

    void setFocus(float xRatio, float yRatio);

    void setFocusMode(int mode);

    void setExposureCompensationRatio(float value);

    void stopPreview();

    void release();

    void takePhoto();

    void setOnFrameCallback(OnFrameCallback callback);

    void setOnFrameCallbackInternal(CameraProxy.OnFrameCallBackInternal callback);

    void setOnTextureIdCallback();

}
