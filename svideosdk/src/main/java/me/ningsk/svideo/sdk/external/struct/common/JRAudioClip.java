package me.ningsk.svideo.sdk.external.struct.common;

/**
 * <p>描述：JRAudioClip<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 16 23<br>
 * 版本：v1.0<br>
 */
public class JRAudioClip {
    private int mId;
    private long mStartTime;
    private long mEndTime;
    private String mFilePath;

    public JRAudioClip(int id, long startTime, String filePath) {
        this.mId = id;
        this.mStartTime = startTime;
        this.mFilePath = filePath + ".aac";
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public void setEndTime(long endTime) {
        this.mEndTime = endTime;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
    }

    public long getDuration() {
        return this.mEndTime - this.mStartTime;
    }

    public String toString() {
        return "JRAudioClip{mId=" + this.mId + ", mStartTime=" + this.mStartTime + ", mEndTime=" + this.mEndTime + ", mPcmFilePath='" + this.mFilePath + '\'' + '}';
    }
}

