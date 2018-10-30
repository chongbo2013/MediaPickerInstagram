package me.ningsk.videorecord.recorder;

import android.content.Context;

import me.ningsk.videorecord.camera.Size;
import me.ningsk.videorecord.gles.EglCore;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 09 16<br>
 * 版本：v1.0<br>
 */
public interface ICameraPreview {

    Size getSurfaceSize();

    Context getContext();

    /**
     * @return object
     * if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture)) {
     *  throw new RuntimeException("invalid surface: " + surface);
     * }
     * @param eglCore
     */
    Object getSurface(EglCore eglCore);
}

