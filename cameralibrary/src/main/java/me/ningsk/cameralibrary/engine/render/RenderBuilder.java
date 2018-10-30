package me.ningsk.cameralibrary.engine.render;

import android.content.Context;

import me.ningsk.cameralibrary.engine.camera.CameraParam;
import me.ningsk.cameralibrary.engine.listener.OnCameraCallback;
import me.ningsk.cameralibrary.engine.listener.OnCaptureListener;
import me.ningsk.cameralibrary.engine.listener.OnFpsListener;

/**
 * <p>描述：渲染引擎Builder<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 45<br>
 * 版本：v1.0<br>
 */
public final class RenderBuilder {

    private PreviewRenderer mPreviewRenderer;
    private CameraParam mCameraParam;

    RenderBuilder(PreviewRenderer renderer, OnCameraCallback callback) {
        mPreviewRenderer = renderer;
        mCameraParam = CameraParam.getInstance();
        mCameraParam.cameraCallback = callback;
    }

    /**
     * 设置拍照回调
     * @param callback
     */
    public RenderBuilder setCaptureFrameCallback(OnCaptureListener callback) {
        mCameraParam.captureCallback = callback;
        return this;
    }

    /**
     * 设置fps回调
     * @param callback
     */
    public RenderBuilder setFpsCallback(OnFpsListener callback) {
        mCameraParam.fpsCallback = callback;
        return this;
    }

    /**
     * 初始化渲染器
     * @param context
     */
    public void initRenderer(Context context) {
        mPreviewRenderer.initRenderer(context);
    }
}

