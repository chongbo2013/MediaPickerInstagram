package me.ningsk.cameralibrary.engine.listener;

/**
 * <p>描述：录制监听器<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 30<br>
 * 版本：v1.0<br>
 */
public interface OnRecordListener {
    // 录制已经开始
    void onRecordStarted();

    // 录制时间改变
    void onRecordProgressChanged(long duration);

    // 录制已经结束
    void onRecordFinish();
}
