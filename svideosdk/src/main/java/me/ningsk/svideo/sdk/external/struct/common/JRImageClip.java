package me.ningsk.svideo.sdk.external.struct.common;

import me.ningsk.svideo.sdk.external.struct.MediaType;
import me.ningsk.svideo.sdk.external.struct.effect.TransitionBase;

/**
 * <p>描述：JRImageClip<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 16 21<br>
 * 版本：v1.0<br>
 */
public class JRImageClip extends JRClip {
    private long mDuration;
    private int mRotation;

    private JRImageClip() {
        this.mRotation = -1;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long durationMills) {
        this.mDuration = durationMills;
    }

    public static final class Builder {
        private JRImageClip mClip = new JRImageClip();

        public Builder() {
            this.mClip.setMediaType(MediaType.ANY_IMAGE_TYPE);
        }

        public Builder source(String source) {
            this.mClip.setSource(source);
            return this;
        }

        public Builder transition(TransitionBase transition) {
            this.mClip.setTransition(transition);
            return this;
        }

        public Builder displayMode(JRDisplayMode mode) {
            this.mClip.setDisplayMode(mode);
            return this;
        }

        public Builder duration(long durationMills) {
            this.mClip.setDuration(durationMills);
            return this;
        }

        public Builder id(int id) {
            this.mClip.setId(id);
            return this;
        }

        public JRImageClip build() {
            return this.mClip;
        }
    }
}

