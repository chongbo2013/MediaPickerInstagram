package me.ningsk.svideo.sdk.external.struct.common;

import me.ningsk.svideo.sdk.external.struct.MediaType;
import me.ningsk.svideo.sdk.external.struct.effect.TransitionBase;

/**
 * <p>描述：Clip<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 15 39<br>
 * 版本：v1.0<br>
 */
public class JRClip {
    private String mSource;
    private TransitionBase mTransition;
    private long mDuration;
    private int mRotation = -1;
    private JRDisplayMode mDisplayMode;
    private int mId;
    private MediaType mediaType;

    protected JRClip() {
        this.mDisplayMode = JRDisplayMode.DEFAULT;
        this.mId = 0;
        this.mediaType = MediaType.ANY_VIDEO_TYPE;
    }

    public JRClip(JRClip clip) {
        this.mDisplayMode = JRDisplayMode.DEFAULT;
        this.mId = 0;
        this.mediaType = MediaType.ANY_VIDEO_TYPE;
        this.mSource = clip.mSource;
        this.mTransition = clip.mTransition;
        this.mDisplayMode = clip.mDisplayMode;
        this.mDuration = clip.mDuration;
        this.mRotation = clip.mRotation;
        this.mediaType = clip.mediaType;
        this.mId = clip.mId;
    }

    public JRClip(String source, TransitionBase transition, JRDisplayMode mode, int rotation, int id) {
        this.mDisplayMode = JRDisplayMode.DEFAULT;
        this.mId = 0;
        this.mediaType = MediaType.ANY_VIDEO_TYPE;
        this.mSource = source;
        this.mTransition = transition;
        this.mDisplayMode = mode;
        this.mRotation = rotation;
        this.mId = id;
    }

    public JRClip(String source, TransitionBase transition, long duration, JRDisplayMode mode, int rotation, int id) {
        this.mDisplayMode = JRDisplayMode.DEFAULT;
        this.mId = 0;
        this.mediaType = MediaType.ANY_VIDEO_TYPE;
        this.mSource = source;
        this.mTransition = transition;
        this.mDuration = duration;
        this.mDisplayMode = mode;
        this.mRotation = rotation;
        this.mediaType = MediaType.ANY_IMAGE_TYPE;
        this.mId = id;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getSource() {
        return this.mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public TransitionBase getTransition() {
        return this.mTransition;
    }

    public void setTransition(TransitionBase transition) {
        this.mTransition = transition;
    }

    public JRDisplayMode getDisplayMode() {
        return this.mDisplayMode;
    }

    public void setDisplayMode(JRDisplayMode mode) {
        this.mDisplayMode = mode;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
