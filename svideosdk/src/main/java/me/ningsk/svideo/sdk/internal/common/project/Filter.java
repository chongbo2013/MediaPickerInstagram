package me.ningsk.svideo.sdk.internal.common.project;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 29<br>
 * 版本：v1.0<br>
 */
public class Filter
{
    public static final int FILTER_TYPE_COLOR = 1;
    public static final int FILTER_TYPE_ANIMATION = 2;
    private String mResPath;
    private long mStartTime;
    private long mDuration;
    private int mType;
    private int mId;

    private Filter(Builder builder)
    {
        this.mId = builder.mId;
        this.mResPath = builder.mResPath;
        this.mStartTime = builder.mStartTime;
        this.mDuration = builder.mDuration;
        this.mType = builder.mType;
    }

    public String getResPath()
    {
        return this.mResPath;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public int getType() {
        return this.mType;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        Filter filter = (Filter)o;

        if (this.mStartTime != filter.mStartTime) return false;
        if (this.mDuration != filter.mDuration) return false;

        return filter.mResPath == null ? true : this.mResPath != null ? this.mResPath.equals(filter.mResPath) : false;
    }

    public int hashCode()
    {
        int result = this.mResPath != null ? this.mResPath.hashCode() : 0;
        result = 31 * result + (int)(this.mStartTime ^ this.mStartTime >>> 32);
        result = 31 * result + (int)(this.mDuration ^ this.mDuration >>> 32);

        return result;
    }

    public static final class Builder
    {
        private String mResPath;
        private long mStartTime;
        private long mDuration;
        private int mType;
        private int mId;

        public Builder resPath(String val)
        {
            this.mResPath = val;
            return this;
        }

        public Builder startTime(long val) {
            this.mStartTime = val;
            return this;
        }

        public Builder duration(long val) {
            this.mDuration = val;
            return this;
        }

        public Builder type(int val) {
            this.mType = val;
            return this;
        }

        public Filter build() {
            return new Filter(this);
        }

        public Builder id(int val) {
            this.mId = val;
            return this;
        }
    }
}
