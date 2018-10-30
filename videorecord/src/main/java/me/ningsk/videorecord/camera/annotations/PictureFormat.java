package me.ningsk.videorecord.camera.annotations;

import android.graphics.ImageFormat;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 30<br>
 * 版本：<br>
 */
@IntDef({ImageFormat.NV21, ImageFormat.RGB_565, ImageFormat.JPEG})
@Retention(RetentionPolicy.SOURCE)
public @interface PictureFormat {
}
