package me.ningsk.common.gl;


import android.view.Surface;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGL11;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/16 09 32<br>
 * 版本：v1.0<br>
 */
public class EGLSurface
{
    private static final String TAG = "JROpenGL";
    private javax.microedition.khronos.egl.EGLSurface mEGLSurface = EGL11.EGL_NO_SURFACE;

    private Surface mSurface = null;

    private SurfaceHolder mHolder = null;

    public EGLSurface(Surface surface)
    {
        this.mSurface = surface;
    }

    public EGLSurface(SurfaceHolder holder) {
        this.mHolder = holder;
        this.mSurface = holder.getSurface();
    }

    public Surface getSurface()
    {
        return this.mSurface;
    }

    public SurfaceHolder getSurfaceHolder()
    {
        return this.mHolder;
    }

    public void createEGLSurface(EGLCore eglCore) {
        if (this.mHolder != null)
            this.mEGLSurface = eglCore.createWindowSurface(this.mHolder);
        else
            this.mEGLSurface = eglCore.createWindowSurface(this.mSurface);
    }

    public void releaseEGLSurface(EGLCore eglCore)
    {
        if (this.mEGLSurface != EGL11.EGL_NO_SURFACE) {
            eglCore.releaseSurface(this.mEGLSurface);
            this.mEGLSurface = EGL11.EGL_NO_SURFACE;
        }
    }

    public javax.microedition.khronos.egl.EGLSurface getEGLSurface() {
        return this.mEGLSurface;
    }
}