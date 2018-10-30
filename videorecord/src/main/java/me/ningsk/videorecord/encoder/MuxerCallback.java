package me.ningsk.videorecord.encoder;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 53<br>
 * 版本：<br>
 */
public interface MuxerCallback {

    void onPrepared();

    void onMuxerStarted(String output);

    void onMuxerStopped(String outPutPath);
}
