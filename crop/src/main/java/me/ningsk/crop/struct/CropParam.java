package me.ningsk.crop.struct;


import android.graphics.Rect;

import me.ningsk.svideo.sdk.external.struct.MediaType;
import me.ningsk.svideo.sdk.external.struct.common.ScaleMode;
import me.ningsk.svideo.sdk.external.struct.common.VideoQuality;
import me.ningsk.svideo.sdk.external.struct.encoder.VideoCodecs;

public class CropParam {
    private String mInputPath;
    private String mOutputPath;
    private int outputWidth;
    private int outputHeight;
    private long startTime;
    private long endTime;
    private Rect cropRect;
    private int frameRate = 25;
    private int gop = 5;
    private int mVideoBitrate;
    private VideoQuality quality = VideoQuality.HD;
    private ScaleMode mScaleMode = ScaleMode.LB;
    private boolean isHWAutoSize = true;
    private MediaType mMediaType = MediaType.ANY_VIDEO_TYPE;

    private int mFillColor = android.R.color.black;
    private boolean isUseGPU = false;
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;

    public VideoQuality getQuality() {
        return this.quality == null ? VideoQuality.HD : this.quality;
    }

    public void setQuality(VideoQuality quality) {
        this.quality = quality;
    }

    public void setVideoBitrate(int bitrate) {
        this.mVideoBitrate = bitrate;
    }

    public int getVideoBitrate() {
        return this.mVideoBitrate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int  getFrameRate() {
        return this.frameRate;
    }

    public void setGop(int gop) {
        this.gop = gop;
    }

    public int getGop() {
        return gop;
    }

    public ScaleMode getScaleMode() {
        return mScaleMode;
    }

    public void setScaleMode(ScaleMode scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public void setInputPath(int inputPath) {
        this.mInputPath = mInputPath;
    }

    public String getInputPath() {
        return this.mInputPath;
    }


    public int getOutputWidth()
    {
        return this.outputWidth;
    }

    public String getOutputPath()
    {
        return this.mOutputPath;
    }

    public void setOutputPath(String outputPath)
    {
        this.mOutputPath = outputPath;
    }

    public void setOutputWidth(int outputWidth)
    {
        this.outputWidth = outputWidth;
    }

    public int getOutputHeight()
    {
        return this.outputHeight;
    }

    public void setOutputHeight(int outputHeight)
    {
        this.outputHeight = outputHeight;
    }

    public long getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public long getEndTime()
    {
        return this.endTime;
    }

    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }

    public Rect getCropRect()
    {
        return this.cropRect;
    }

    public void setCropRect(Rect cropRect)
    {
        this.cropRect = cropRect;
    }

    public boolean isHWAutoSize()
    {
        return this.isHWAutoSize;
    }

    public MediaType getMediaType() {
        return this.mMediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mMediaType = mediaType;
    }

    public void setHWAutoSize(boolean HWAutoSize) {
        this.isHWAutoSize = HWAutoSize;
    }

    public int getFillColor() {
        return this.mFillColor;
    }

    public boolean isUseGPU() {
        return this.isUseGPU;
    }

    public void setUseGPU(boolean useGPU) {
        this.isUseGPU = useGPU;
    }

    public void setFillColor(int fillColor)
    {
        this.mFillColor = fillColor;
    }

    public VideoCodecs getVideoCodec()
    {
        return this.mVideoCodec;
    }

    public void setVideoCodec(VideoCodecs videoCodec)
    {
        this.mVideoCodec = videoCodec;
    }
}
