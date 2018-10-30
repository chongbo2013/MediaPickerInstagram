package me.ningsk.videorecord.recorder;

import android.content.Context;

import me.ningsk.videorecord.camera.Size;
import me.ningsk.videorecord.gles.EglCore;
import me.ningsk.videorecord.gles.OffscreenSurface;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 09 18<br>
 * 版本：<br>
 */
public class OffscreenCameraPreview implements ICameraPreview {

    private final Context mContext;
    private final int mWidth;
    private final int mHeight;
    private OffscreenSurface mOffscreenSurface;

    public OffscreenCameraPreview(Context context, int width, int height) {
        mContext = context;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public Size getSurfaceSize() {
        return new Size(mWidth, mHeight);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * @param eglCore
     * @return object
     * if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)) {
     * throw new RuntimeException("invalid surface: " + surface);
     * }
     */
    @Override
    public synchronized Object getSurface(EglCore eglCore) {
        if (mOffscreenSurface == null)
            mOffscreenSurface = new OffscreenSurface(eglCore, mWidth, mHeight);
        return mOffscreenSurface;
    }
}

