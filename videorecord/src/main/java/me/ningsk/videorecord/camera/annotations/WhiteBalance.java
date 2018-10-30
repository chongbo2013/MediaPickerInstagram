package me.ningsk.videorecord.camera.annotations;

import android.hardware.Camera;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>描述：白平衡<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 30<br>
 * 版本：v1.0<br>
 */
@StringDef({
        Camera.Parameters.WHITE_BALANCE_AUTO,
        Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT,
        Camera.Parameters.WHITE_BALANCE_DAYLIGHT,
        Camera.Parameters.WHITE_BALANCE_FLUORESCENT,
        Camera.Parameters.WHITE_BALANCE_INCANDESCENT,
        Camera.Parameters.WHITE_BALANCE_SHADE,
        Camera.Parameters.WHITE_BALANCE_TWILIGHT,
        Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT})
@Retention(RetentionPolicy.SOURCE)
public @interface WhiteBalance {
}
