package me.ningsk.videorecord.recorder;

import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.TextureView;

import me.ningsk.videorecord.camera.Size;
import me.ningsk.videorecord.gles.EglCore;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 09 17<br>
 * 版本：v1.0<br>
 */
public class DefaultCameraPreview implements ICameraPreview {

    protected SurfaceView mSurfaceView;
    protected TextureView mTextureView;
    protected Context mContext;

    public DefaultCameraPreview(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mContext = surfaceView.getContext();
    }

    public DefaultCameraPreview(TextureView textureView) {
        mTextureView = textureView;
        mContext = textureView.getContext();
    }


    @Override
    public Size getSurfaceSize() {
        if (mSurfaceView != null) {
            Rect surfaceFrame = mSurfaceView.getHolder().getSurfaceFrame();
            return new Size(surfaceFrame.width(), surfaceFrame.height());
        } else if (mTextureView != null) {
            return new Size(mTextureView.getWidth(), mTextureView.getHeight());
        }
        return new Size(0, 0);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Object getSurface(EglCore eglCore) {
        if (mSurfaceView != null) {
            return mSurfaceView.getHolder().getSurface();
        } else if (mTextureView != null) {
            return mTextureView.getSurfaceTexture();
        }
        return null;
    }

}

