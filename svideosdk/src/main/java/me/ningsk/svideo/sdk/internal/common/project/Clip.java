package me.ningsk.svideo.sdk.internal.common.project;

import java.io.File;

import me.ningsk.svideo.sdk.external.struct.MediaType;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 17 58<br>
 * 版本：v1.0<br>
 */
public class Clip {
    private String src;
    private long mFadeDuration;
    private int mInAnimation;
    private int mOutAnimation;
    private int mDisplayMode;
    private long mDuration;
    private int gop;
    private int mBitrate;
    private int quality;
    private MediaType mediaType = MediaType.ANY_VIDEO_TYPE;
    private long startTime;
    private long endTime;
    public int mediaWidth =0;
    public int mediaHeight = 0;
    public int rotation;

    public void setPath(String path) {
        this.src = path;
    }

    public String getPath() {
        return this.src;
    }

    public int getGop()
    {
        return this.gop;
    }

    public void setGop(int gop) {
        this.gop = gop;
    }

    public int getBitrate()
    {
        return this.mBitrate;
    }

    public void setBitrate(int bitrate) {
        this.mBitrate = bitrate;
    }

    public int getQuality()
    {
        return this.quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isImage()
    {
        return this.mediaType.equals(MediaType.ANY_IMAGE_TYPE);
    }

    public boolean isVideo() {
        return this.mediaType.equals(MediaType.ANY_VIDEO_TYPE);
    }

    public long getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime()
    {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDurationMilli()
    {
        return this.endTime - this.startTime;
    }

    public int getRotation()
    {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getMediaWidth() {
        return this.mediaWidth;
    }

    public void setMediaWidth(int mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    public int getMediaHeight() {
        return this.mediaHeight;
    }

    public void setMediaHeight(int mediaHeight) {
        this.mediaHeight = mediaHeight;
    }

    public String toString()
    {
        return "[videoFile:" + this.src + ", duration:" + getDurationMilli() + "]";
    }

    public boolean validate()
    {
        if (!new File(this.src).exists()) {
            return false;
        }
        if ((isVideo()) && (getDurationMilli() <= 0L)) {
            return false;
        }

        return true;
    }

    public long getFadeDuration() {
        return this.mFadeDuration;
    }

    public void setFadeDuration(long fadeDuration) {
        this.mFadeDuration = fadeDuration;
    }

    public int getInAnimation() {
        return this.mInAnimation;
    }

    public void setInAnimation(int mInAnimation) {
        this.mInAnimation = mInAnimation;
    }

    public int getOutAnimation() {
        return this.mOutAnimation;
    }

    public void setOutAnimation(int mOutAnimation) {
        this.mOutAnimation = mOutAnimation;
    }

    public int getDisplayMode() {
        return this.mDisplayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.mDisplayMode = displayMode;
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

}
