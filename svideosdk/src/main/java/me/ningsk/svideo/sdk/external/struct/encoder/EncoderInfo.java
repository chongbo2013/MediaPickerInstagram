package me.ningsk.svideo.sdk.external.struct.encoder;

/**
 * <p>描述：编码信息<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 14<br>
 * 版本：v1.0<br>
 */
public class EncoderInfo {
    public long encoderType;
    public long width;
    public long height;
    public long duration;
    public long fps;
    public long bitrateDiff;
    public long keyframeDelay;
    public long avgUseTime;
    public long maxCacheFrame;

    @Override
    public String toString() {
        return "EncoderInfo{encoderType=" + this.encoderType + ", width=" + this.width + ", height=" + this.height
                + ", duration=" + this.duration + ", fps=" + this.fps + ", bitrateDiff=" + this.bitrateDiff
                + ", keyframeDelay=" + this.keyframeDelay + ", avgUseTime=" + this.avgUseTime + ", maxCacheFrame="
                + this.maxCacheFrame + '}';

    }
}
