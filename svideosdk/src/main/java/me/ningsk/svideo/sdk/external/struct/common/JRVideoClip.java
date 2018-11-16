package me.ningsk.svideo.sdk.external.struct.common;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import me.ningsk.svideo.sdk.external.struct.MediaType;
import me.ningsk.svideo.sdk.external.struct.effect.TransitionBase;

/**
 * <p>描述：JRVideoClip<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 16 18<br>
 * 版本：v1.0<br>
 */
public class JRVideoClip extends JRClip{
    private long mStartTime = 0L;
    private long mEndTime = 0L;
    private int mRotation = -1;

    private JRVideoClip() {
        this.mStartTime = 0L;
        this.mEndTime = 0L;
        this.mRotation = -1;
    }


    public long getStartTime()
    {
        return this.mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public long getEndTime()
    {
        return this.mEndTime;
    }

    public void setEndTime(long endTime) {
        this.mEndTime = endTime;
    }

    public int getRotation()
    {
        return this.mRotation;
    }

    public void setRotation(int rotation)
    {
        this.mRotation = rotation;
    }
    public static final class Builder {
        private JRVideoClip mClip;

        public Builder() {
            this.mClip = new JRVideoClip();
            this.mClip.setMediaType(MediaType.ANY_VIDEO_TYPE);
        }

        public Builder rotation(int rotation)
        {
            this.mClip.setRotation(rotation);
            return this;
        }

        public Builder source(String source)
        {
            this.mClip.setSource(source);
            return this;
        }

        public Builder transition(TransitionBase transition)
        {
            this.mClip.setTransition(transition);
            return this;
        }

        public Builder displayMode(JRDisplayMode mode)
        {
            this.mClip.setDisplayMode(mode);
            return this;
        }

        public Builder id(int id)
        {
            this.mClip.setId(id);
            return this;
        }

        public Builder startTime(long startTimeMills)
        {
            this.mClip.setStartTime(startTimeMills);
            return this;
        }

        public Builder endTime(long endTimeMills)
        {
            this.mClip.setEndTime(endTimeMills);
            return this;
        }

        public JRVideoClip build() {
            if (TextUtils.isEmpty(this.mClip.getSource())) {
                Log.e("JRLog", "Source is null!");
                return null;
            }
            if (this.mClip.getEndTime() == 0L) {
                try {
                    MediaMetadataRetriever localMediaMetadataRetriever = new MediaMetadataRetriever();
                    localMediaMetadataRetriever.setDataSource(this.mClip.getSource());
                    this.mClip.setEndTime(Long.parseLong(localMediaMetadataRetriever.extractMetadata(9)));
                } catch (Exception localException) {
                    Log.e("JRLog", "Invalid source[" + this.mClip.getSource() + "]");
                    return null;
                }
            }

            return this.mClip;
        }
    }
}
