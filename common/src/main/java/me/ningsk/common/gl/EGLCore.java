package me.ningsk.common.gl;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLExt;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;

/**
 * <p>描述：
 * Core EGL state (display, context, config).
 * The EGLContext must only be attached to one thread at a time.  This class is not thread-safe.
 * 作者：ningsk<br>
 * 日期：2018/11/16 09 28<br>
 * 版本：v1.0<br>
 */
public class EGLCore
{
    private static final String TAG = "EGLCore";
    public static final int FLAG_RECORDABLE = 1;
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    private EGL10 mEGL = (EGL10) EGLContext.getEGL();
    private javax.microedition.khronos.egl.EGLDisplay mEGLDisplay = EGL11.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL11.EGL_NO_CONTEXT;
    private EGLConfig mEGLConfig = null;
    private String mEglExtensions;

    public EGLCore(EGLContext sharedContext, int flags)
    {
        if (sharedContext == null) {
            sharedContext = EGL11.EGL_NO_CONTEXT;
        }

        this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL11.EGL_DEFAULT_DISPLAY);
        if (this.mEGLDisplay == EGL11.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }

        if (!this.mEGL.eglInitialize(this.mEGLDisplay, null)) {
            this.mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }

        this.mEglExtensions = this.mEGL.eglQueryString(this.mEGLDisplay, 12373);
        EGLConfig config = getConfig(flags, 2);
        if (config == null) {
            throw new RuntimeException("Unable to find a suitable EGLConfig");
        }

        int EGL_CONTEXT_CLIENT_VERSION = 12440;
        int[] attrib2_list = { EGL_CONTEXT_CLIENT_VERSION, 2, 12344 };

        EGLContext context = this.mEGL.eglCreateContext(this.mEGLDisplay, config, sharedContext, attrib2_list);
        checkEglError("eglCreateContext");
        this.mEGLConfig = config;
        this.mEGLContext = context;
    }

    private EGLConfig getConfig(int flags, int version)
    {
        int renderableType = 4;

        int[] attribList = { 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, renderableType, 12339, 5, 12344, 0, 12344 };

        if (((flags & 0x1) != 0) &&
                (this.mEglExtensions != null)) {
            if ((this.mEglExtensions.contains("EGL_ANDROID_recordable")) &&
                    ((!Build.MODEL
                            .equals("M351")) ||
                            (Build.VERSION.SDK_INT != 19)) &&
                    ((!Build.MODEL
                            .equals("GT-I9500")) ||
                            (Build.VERSION.SDK_INT != 19)) && (
                    (!Build.MODEL
                            .equals("M355")) ||
                            (Build.VERSION.SDK_INT != 19))) {
                attribList[(attribList.length - 3)] = 12610;
                attribList[(attribList.length - 2)] = 1;
            } else {
                Log.d(TAG, "Extensions = " + this.mEglExtensions);
            }
        }

        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!this.mEGL.eglChooseConfig(this.mEGLDisplay, attribList, configs, configs.length, numConfigs)) {
            Log.w(TAG, "unable to find RGB8888 / " + version + " EGLConfig");
            return null;
        }
        return configs[0];
    }

    public void release()
    {
        if (this.mEGLContext != EGL11.EGL_NO_CONTEXT)
        {
            this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL11.EGL_NO_SURFACE, EGL11.EGL_NO_SURFACE, EGL11.EGL_NO_CONTEXT);

            this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);

            this.mEGL.eglTerminate(this.mEGLDisplay);
        }

        this.mEGLDisplay = EGL11.EGL_NO_DISPLAY;
        this.mEGLContext = EGL11.EGL_NO_CONTEXT;
        this.mEGLConfig = null;
        this.mEGL = null;
    }

    public void releaseSurface(javax.microedition.khronos.egl.EGLSurface eglSurface)
    {
        if (eglSurface != null) {
            this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL11.EGL_NO_SURFACE, EGL11.EGL_NO_SURFACE, EGL11.EGL_NO_CONTEXT);

            Log.d(TAG, "before release surface, " + eglSurface);
            this.mEGL.eglDestroySurface(this.mEGLDisplay, eglSurface);
            Log.d(TAG, "after release surface, " + eglSurface);
        }
    }

    public javax.microedition.khronos.egl.EGLSurface createWindowSurface(Object surface)
    {
        Log.d(TAG, "create window surface, surface " + surface);
        if ((!(surface instanceof Surface)) && (!(surface instanceof SurfaceTexture)) && (!(surface instanceof SurfaceHolder)))
        {
            throw new RuntimeException("invalid surface: " + surface);
        }

        int[] surfaceAttributes = { 12344 };
        Log.d(TAG, "before create new egl surface");

        javax.microedition.khronos.egl.EGLSurface eglSurface = this.mEGL.eglCreateWindowSurface(this.mEGLDisplay, this.mEGLConfig, surface, surfaceAttributes);

        Log.d(TAG, "after create new egl surface, " + eglSurface);
        checkEglError("eglCreateWindowSurface");
        if (eglSurface == null) {
            throw new RuntimeException("surface was null");
        }
        return eglSurface;
    }

    public javax.microedition.khronos.egl.EGLSurface createPBufferSurface(int w, int h)
    {
        int[] surfaceAttributes = { 12375, w, 12374, h, 12344 };

        javax.microedition.khronos.egl.EGLSurface eglSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, this.mEGLConfig, surfaceAttributes);
        if (eglSurface == EGL10.EGL_NO_SURFACE) {
            checkEglError("createPBufferSurface");
            throw new RuntimeException("surface was null");
        }
        return eglSurface;
    }

    public void makeCurrent(javax.microedition.khronos.egl.EGLSurface eglSurface)
    {
        if (this.mEGLDisplay == EGL11.EGL_NO_DISPLAY)
        {
            Log.d(TAG, "NOTE: makeCurrent w/o display");
        }
        if (!this.mEGL.eglMakeCurrent(this.mEGLDisplay, eglSurface, eglSurface, this.mEGLContext)) {
            checkEglError("Make current");
            throw new RuntimeException("eglMakeCurrent failed");
        }
        Log.d(TAG, "after make current， " + eglSurface);
    }

    public boolean swapBuffers(javax.microedition.khronos.egl.EGLSurface eglSurface)
    {
        return this.mEGL.eglSwapBuffers(this.mEGLDisplay, eglSurface);
    }

    @RequiresApi(api=18)
    public void setPresentationTime(long nsecs)
    {
        if ((this.mEglExtensions != null) &&
                (this.mEglExtensions.contains("EGL_ANDROID_presentation_time"))) {
            android.opengl.EGLDisplay display = EGL14.eglGetCurrentDisplay();
            android.opengl.EGLSurface surface = EGL14.eglGetCurrentSurface(12377);
            EGLExt.eglPresentationTimeANDROID(display, surface, nsecs);
        }
    }

    private void checkEglError(String msg)
    {
        int error;
        if ((error = this.mEGL.eglGetError()) != 12288)
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
    }
}

