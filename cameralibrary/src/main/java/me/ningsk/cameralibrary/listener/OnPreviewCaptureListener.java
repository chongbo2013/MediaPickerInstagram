package me.ningsk.cameralibrary.listener;

import me.ningsk.cameralibrary.engine.model.GalleryType;

/**
 * <p>描述：媒体拍摄回调<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 41<br>
 * 版本：v1.0<br>
 */
public interface OnPreviewCaptureListener {

    // 媒体选择
    void onMediaSelectedListener(String path, GalleryType type);
}

