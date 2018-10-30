package me.ningsk.videorecord.camera.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 29<br>
 * 版本：<br>
 */
@IntDef({
        android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK,
        android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT})
@Retention(RetentionPolicy.SOURCE)
public @interface Facing {
}
