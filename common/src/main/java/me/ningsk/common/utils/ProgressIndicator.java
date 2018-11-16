package me.ningsk.common.utils;

public interface ProgressIndicator
{
    long getDuration();

    void setDurationLimit(long paramLong);

    void setProgressListener(ProgressListener paramProgressListener);

    interface ProgressListener
    {
        void onProgress(long paramLong);

        void onLimitReached();
    }
}
