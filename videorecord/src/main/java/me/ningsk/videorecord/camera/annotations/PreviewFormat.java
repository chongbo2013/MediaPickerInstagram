package me.ningsk.videorecord.camera.annotations;

import android.graphics.ImageFormat;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 35<br>
 * 版本：<br>
 */
@IntDef({ImageFormat.NV21, ImageFormat.YV12})
@Retention(RetentionPolicy.SOURCE)
public @interface PreviewFormat {
}
