package me.ningsk.svideo.sdk.internal.common.project;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 03<br>
 * 版本：v1.0<br>
 */
public class AudioMix {
    public int mId;
    public String mPath;
    public long mStartTime;
    public long mDuration;
    public long mStreamStartTime;
    public long mStreamDuration;
    public int mWeight;
    public boolean mDenoise;

    private AudioMix() {}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AudioMix) {
            return this.mId == ((AudioMix)obj).mId;
        } else {
            return false;
        }
    }
}
