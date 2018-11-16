package me.ningsk.svideo.sdk.external.struct.effect;

public class TransitionBase {
    protected static final int TRANSITION_TYPE_SHUTTER = 0;
    protected static final int TRANSITION_TYPE_TRANSLATE = 1;
    protected static final int TRANSITION_TYPE_CIRCLE = 2;
    protected static final int TRANSITION_TYPE_FIVEPOINTSTAR = 3;
    protected static final int TRANSITION_TYPE_FADE = 4;
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_UP = 2;
    public static final int DIRECTION_DOWN = 3;
    protected int mType;
    protected long mOverlapDuration;
    protected float mLineWidth;
    protected int mOrientation;
    protected int mDirection;

    public TransitionBase() {
    }

    public long getOverlapDuration() {
        return this.mOverlapDuration;
    }

    public void setOverlapDuration(long overlapDurationUs) {
        this.mOverlapDuration = overlapDurationUs;
    }
}

