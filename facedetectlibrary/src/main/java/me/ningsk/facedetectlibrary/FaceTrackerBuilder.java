package me.ningsk.facedetectlibrary;

import me.ningsk.listener.FaceTrackerCallback;

/**
 * <p>描述：人脸检测构造器<p>
 * 作者：ningsk<br>
 * 日期：2018/10/31 08 33<br>
 * 版本：v1.0<br>
 */
public final class FaceTrackerBuilder {

    private FaceTracker mFaceTracker;
    private FaceTrackParam mFaceTrackParam;

    public FaceTrackerBuilder(FaceTracker tracker, FaceTrackerCallback callback) {
        mFaceTracker = tracker;
        mFaceTrackParam = FaceTrackParam.getInstance();
        mFaceTrackParam.trackerCallback = callback;
    }

    /**
     * 准备检测器
     */
    public void initTracker() {
        mFaceTracker.initTracker();
    }

    /**
     * 是否预览检测
     * @param previewTrack
     * @return
     */
    public FaceTrackerBuilder previewTrack(boolean previewTrack) {
        mFaceTrackParam.previewTrack = previewTrack;
        return this;
    }

}

