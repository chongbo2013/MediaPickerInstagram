package me.ningsk.svideo.sdk.external.struct.effect;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 33<br>
 * 版本：v1.0<br>
 */
public class ActionBase {
    protected static final int FRAME_ANIMATION_TYPE_FADE = 0;
    protected static final int FRAME_ANIMATION_TYPE_SCALE = 1;
    protected static final int FRAME_ANIMATION_TYPE_TRANSLATE = 2;
    protected static final int FRAME_ANIMATION_TYPE_ROTATE_REPEAT = 3;
    protected static final int FRAME_ANIMATION_TYPE_ROTATE_BY = 4;
    protected static final int FRAME_ANIMATION_TYPE_ROTATE_TO = 5;
    protected static final int FRAME_ANIMATION_TYPE_LINEAR_WIPE = 6;
    protected int mTargetId;
    protected int mType;
    protected long mStartTime;
    protected long mDuration;
    protected float mAlpha;
    protected boolean mIsStream;
    protected int mId;
    protected String mAnimationConfig;
    protected float mFromPointX;
    protected float mFromPointY;
    protected float mToPointX;
    protected float mToPointY;
    protected boolean mClockwise;
    protected float mFromDegree;
    protected float mRotateDegree;
    protected float mRotateToDegree;
    protected boolean mRepeat;
    protected float mDurationPerCircle;
    protected float mFromScale;
    protected float mToScale;
    protected float mFromAlpha;
    protected float mToAlpha;
    protected int mDirection;
    protected int mWipeMode;

    public ActionBase() {
    }

    public String getAnimationConfig() {
        return this.mAnimationConfig;
    }

    public void setAnimationConfig(String animationConfig) {
        this.mAnimationConfig = animationConfig;
    }

    public int getType() {
        return this.mType;
    }

    protected void setType(int type) {
        this.mType = type;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long startTimeUs) {
        this.mStartTime = startTimeUs;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long durationUs) {
        this.mDuration = durationUs;
    }

    public boolean isStream() {
        return this.mIsStream;
    }

    public void setIsStream(boolean isStream) {
        this.mIsStream = isStream;
    }

    public int getTargetId() {
        return this.mTargetId;
    }

    public void setTargetId(int id) {
        this.mTargetId = id;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ActionBase) {
            return ((ActionBase)obj).mId == this.mId;
        } else {
            return false;
        }
    }
}

