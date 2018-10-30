package me.ningsk.videorecord.camera.annotations;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>描述：防闪烁<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 29<br>
 * 版本：v1.0<br>
 */
@StringDef({
        android.hardware.Camera.Parameters.ANTIBANDING_50HZ,
        android.hardware.Camera.Parameters.ANTIBANDING_60HZ,
        android.hardware.Camera.Parameters.ANTIBANDING_AUTO,
        android.hardware.Camera.Parameters.ANTIBANDING_OFF})
@Retention(RetentionPolicy.SOURCE)
public @interface Antibanding {
}
