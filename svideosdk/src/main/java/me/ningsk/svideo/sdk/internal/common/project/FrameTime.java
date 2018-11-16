package me.ningsk.svideo.sdk.internal.common.project;

import java.io.Serializable;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 27<br>
 * 版本：v1.0<br>
 */
public class FrameTime implements Serializable {
    private static final long serialVersionUID = 7821490823848983173L;
    public double beginTime;
    public double endTime;
    public int shrink;
    public double minTime;
    public double maxTime;

    public FrameTime() {
    }

    public double getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(double beginTime) {
        this.beginTime = beginTime;
    }

    public double getEndTime() {
        return this.endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public int getShrink() {
        return this.shrink;
    }

    public void setShrink(int shrink) {
        this.shrink = shrink;
    }

    public double getMinTime() {
        return this.minTime;
    }

    public void setMinTime(double minTime) {
        this.minTime = minTime;
    }

    public double getMaxTime() {
        return this.maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }
}

