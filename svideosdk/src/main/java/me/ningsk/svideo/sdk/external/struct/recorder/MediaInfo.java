package me.ningsk.svideo.sdk.external.struct.recorder;

import me.ningsk.svideo.sdk.external.struct.encoder.VideoCodecs;

/**
 * <p>描述：媒体信息<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 32<br>
 * 版本：v1.0<br>
 */
public class MediaInfo {

    private int videoWidth;
    private int videoHeight;
    private int fps = 25;
    private VideoCodecs mVideoCodec;
    private int mCrf;
    private int mEncoderFps;

    public MediaInfo() {
        this.mVideoCodec = VideoCodecs.H264_HARDWARE;
        this.mCrf = 23;
        this.mEncoderFps = 25;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public VideoCodecs getmVideoCodec() {
        return mVideoCodec;
    }

    public void setmVideoCodec(VideoCodecs mVideoCodec) {
        this.mVideoCodec = mVideoCodec;
    }

    public int getmCrf() {
        return mCrf;
    }

    public void setmCrf(int mCrf) {
        this.mCrf = mCrf;
    }

    public int getmEncoderFps() {
        return mEncoderFps;
    }

    public void setmEncoderFps(int mEncoderFps) {
        this.mEncoderFps = mEncoderFps;
    }
}
