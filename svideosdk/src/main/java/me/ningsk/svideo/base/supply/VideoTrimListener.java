package me.ningsk.svideo.base.supply;

/**
 * <p>描述：裁剪监听<p>
 * 作者：ningsk<br>
 * 日期：2018/11/13 16 52<br>
 * 版本：v1.0<br>
 */
public interface VideoTrimListener {

    void onStartTrim();
    void onFinishTrim();
    void onCancel();

}
