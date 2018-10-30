package me.ningsk.cameralibrary.engine.listener;

import java.nio.ByteBuffer;

/**
 * <p>描述：截帧监听器<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 35<br>
 * 版本：<br>
 */
public interface OnCaptureListener {
    // 截帧回调
    void onCapture(ByteBuffer buffer, int width, int height);
}
