package me.ningsk.svideo.sdk.external.struct.snap;

import me.ningsk.svideo.sdk.external.struct.common.VideoDisplayMode;
import me.ningsk.svideo.sdk.external.struct.common.VideoQuality;
import me.ningsk.svideo.sdk.external.struct.encoder.VideoCodecs;
import me.ningsk.svideo.sdk.external.struct.recorder.CameraType;
import me.ningsk.svideo.sdk.external.struct.recorder.FlashType;

/**
 * <p>描述：Snap参数<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 14 39<br>
 * 版本：v1.0<br>
 */
public class JRSnapVideoParam {
    public static final String CROP_MODE = "crop_mode";
    public static final String VIDEO_FRAMERATE = "video_framerate";
    public static final String VIDEO_GOP = "video_gop";
    public static final String VIDEO_BITRATE = "video_bitrate";
    public static final String NEED_RECORD = "need_record";
    public static final String MAX_VIDEO_DURATION = "max_video_duration";
    public static final String MIN_VIDEO_DURATION = "min_video_duration";
    public static final String MIN_CROP_DURATION = "min_crop_duration";
    public static final String VIDEO_RESOLUTION = "video_resolution";
    public static final String MIN_DURATION = "min_duration";
    public static final String MAX_DURATION = "max_duration";
    public static final String VIDEO_QUALITY = "video_quality";
    public static final String VIDEO_RATIO = "video_ratio";
    public static final String RECORD_MODE = "record_mode";
    public static final String FILTER_LIST = "filter_list";
    public static final String BEAUTY_STATUS = "beauty_status";
    public static final String BEAUTY_LEVEL = "beauty_level";
    public static final String CAMERA_TYPE = "camera_type";
    public static final String FLASH_TYPE = "flash_type";
    public static final String NEED_CLIP = "need_clip";
    public static final String SORT_MODE = "sort_mode";
    public static final String CROP_USE_GPU = "crop_use_gpu";
    public static final String VIDEO_CODEC = "video_codec";
    public static final VideoDisplayMode SCALE_CROP;
    public static final VideoDisplayMode SCALE_FILL;
    public static final int SORT_MODE_VIDEO = 0;
    public static final int SORT_MODE_PHOTO = 1;
    public static final int SORT_MODE_MERGE = 2;
    public static final int RATIO_MODE_3_4 = 0;
    public static final int RATIO_MODE_1_1 = 1;
    public static final int RATIO_MODE_9_16 = 2;
    public static final int RESOLUTION_360P = 0;
    public static final int RESOLUTION_480P = 1;
    public static final int RESOLUTION_540P = 2;
    public static final int RESOLUTION_720P = 3;
    public static final int RECORD_MODE_TOUCH = 0;
    public static final int RECORD_MODE_PRESS = 1;
    public static final int RECORD_MODE_AUTO = 2;
    private int mResolutionMode = RESOLUTION_540P;
    private int mRatioMode = RATIO_MODE_1_1;
    private boolean mNeedRecord = true;
    private VideoQuality mVideoQuality;
    private int mGop;
    private int mFrameRate;
    private int mBitrate;
    private int mMinVideoDuration;
    private int mMaxVideoDuration;
    private int mMinCropDuration;
    private VideoDisplayMode mScaleMode;
    private int mRecordMode;
    private String[] mFilterList;
    private boolean mBeautyStatus;
    private int mBeautyLevel;
    private CameraType mCameraType;
    private FlashType mFlashType;
    private boolean mNeedClip;
    private int mMaxDuration;
    private int mMinDuration;
    private int mSortMode;
    private boolean isCropUseGPU;
    private VideoCodecs mVideoCodec;

    public JRSnapVideoParam() {
        this.mVideoQuality = VideoQuality.HD;
        this.mGop = 5;
        this.mFrameRate = 25;
        this.mMinVideoDuration = 2000;
        this.mMaxVideoDuration = 600000;
        this.mMinCropDuration = 3000;
        this.mScaleMode = VideoDisplayMode.SCALE;
        this.mRecordMode = RESOLUTION_540P;
        this.mBeautyStatus = true;
        this.mBeautyLevel = 80;
        this.mCameraType = CameraType.FRONT;
        this.mFlashType = FlashType.OFF;
        this.mNeedClip = true;
        this.mMaxDuration = 30000;
        this.mMinVideoDuration = 2000;
        this.mSortMode = SORT_MODE_MERGE;
        this.isCropUseGPU = false;
        this.mVideoCodec = VideoCodecs.H264_HARDWARE;
    }

    public int getSortMode() {
        return this.mSortMode;
    }

    public void setSortMode(int sortMode) {
        this.mSortMode = sortMode;
    }

    public void setCropUseGPU(boolean isUseGPU) {
        this.isCropUseGPU = isUseGPU;
    }

    public int getRecordMode() {
        return this.mRecordMode;
    }

    public void setRecordMode(int mRecordMode) {
        this.mRecordMode = mRecordMode;
    }

    public boolean isNeedClip() {
        return this.mNeedClip;
    }

    public void setNeedClip(boolean mNeedClip) {
        this.mNeedClip = mNeedClip;
    }

    public boolean getBeautyStatus() {
        return this.mBeautyStatus;
    }

    public void setBeautyStatus(boolean mBeautyStatus) {
        this.mBeautyStatus = mBeautyStatus;
    }

    public String[] getFilterList() {
        return this.mFilterList;
    }

    public void setFilterList(String[] mFilterList) {
        this.mFilterList = mFilterList;
    }

    public int getBeautyLevel() {
        return this.mBeautyLevel;
    }

    public CameraType getCameraType() {
        return this.mCameraType;
    }

    public void setCameraType(CameraType mCameraType) {
        this.mCameraType = mCameraType;
    }

    public FlashType getFlashType() {
        return this.mFlashType;
    }

    public void setFlashType(FlashType mFlashType) {
        this.mFlashType = mFlashType;
    }

    public void setBeautyLevel(int mBeautyLevel) {
        this.mBeautyLevel = mBeautyLevel;
    }

    public int getMaxDuration() {
        return this.mMaxDuration;
    }

    public void setMaxDuration(int mMaxDuration) {
        this.mMaxDuration = mMaxDuration;
    }

    public int getMinDuration() {
        return this.mMinDuration;
    }

    public void setMinDuration(int mMinDuration) {
        this.mMinDuration = mMinDuration;
    }

    public int getMinVideoDuration() {
        return this.mMinVideoDuration;
    }

    public void setMinVideoDuration(int mMinVideoDuration) {
        this.mMinVideoDuration = mMinVideoDuration;
    }

    public int getMaxVideoDuration() {
        return this.mMaxVideoDuration;
    }

    public void setMaxVideoDuration(int mMaxVideoDuration) {
        this.mMaxVideoDuration = mMaxVideoDuration;
    }

    public int getMinCropDuration() {
        return this.mMinCropDuration;
    }

    public void setMinCropDuration(int mMinCropDuration) {
        this.mMinCropDuration = mMinCropDuration;
    }

    public boolean isCropUseGPU() {
        return this.isCropUseGPU;
    }

    public int getFrameRate() {
        return this.mFrameRate;
    }

    public void setFrameRate(int mFrameRate) {
        this.mFrameRate = mFrameRate;
    }

    public VideoDisplayMode getScaleMode() {
        return this.mScaleMode;
    }

    public void setScaleMode(VideoDisplayMode mCropMode) {
        this.mScaleMode = mCropMode;
    }

    public int getResolutionMode() {
        return this.mResolutionMode;
    }

    public void setResolutionMode(int mResolutionMode) {
        this.mResolutionMode = mResolutionMode;
    }

    public int getRatioMode() {
        return this.mRatioMode;
    }

    public void setRatioMode(int mRatioMode) {
        this.mRatioMode = mRatioMode;
    }

    public boolean isNeedRecord() {
        return this.mNeedRecord;
    }

    public void setNeedRecord(boolean mNeedClip) {
        this.mNeedRecord = mNeedClip;
    }

    public VideoQuality getVideoQuality() {
        return this.mVideoQuality;
    }

    public void setVideoQuality(VideoQuality mVideoQuality) {
        this.mVideoQuality = mVideoQuality;
    }

    public int getGop() {
        return this.mGop;
    }

    public void setGop(int mGop) {
        this.mGop = mGop;
    }

    public void setVideoBitrate(int bitrate) {
        this.mBitrate = bitrate;
    }

    public int getVideoBitrate() {
        return this.mBitrate;
    }

    public void setVideoCodec(VideoCodecs codec) {
        this.mVideoCodec = codec;
    }

    public VideoCodecs getVideoCodec() {
        return this.mVideoCodec;
    }

    static {
        SCALE_CROP = VideoDisplayMode.SCALE;
        SCALE_FILL = VideoDisplayMode.FILL;
    }

    public static class Builder {
        private JRSnapVideoParam mParam = new JRSnapVideoParam();

        public Builder() {
        }

        public Builder setResolutionMode(int resolutionMode) {
            this.mParam.setResolutionMode(resolutionMode);
            return this;
        }

        public Builder setRatioMode(int ratioMode) {
            this.mParam.setRatioMode(ratioMode);
            return this;
        }

        public Builder setNeedRecord(boolean needClip) {
            this.mParam.setNeedRecord(needClip);
            return this;
        }

        public Builder setVideoQuality(VideoQuality videoQuality) {
                this.mParam.setVideoQuality(videoQuality);
                return this;
        }

        public Builder setGop(int gop) {
            this.mParam.setGop(gop);
            return this;
        }

        public Builder setCropMode(VideoDisplayMode scaleMode) {
            this.mParam.setScaleMode(scaleMode);
            return this;
        }

        public Builder setMinVideoDuration(int duration) {
            this.mParam.setMinVideoDuration(duration);
            return this;
        }

        public Builder setMaxVideoDuration(int duration) {
            this.mParam.setMaxVideoDuration(duration);
            return this;
        }

        public Builder setRecordMode(int recordMode) {
            this.mParam.setRecordMode(recordMode);
            return this;
        }

        public Builder setMinCropDuration(int duration) {
            this.mParam.setMinCropDuration(duration);
            return this;
        }

        public Builder setFilterList(String[] filterList) {
            this.mParam.setFilterList(filterList);
            return this;
        }

        public Builder setBeautyLevel(int beautyLevel) {
            this.mParam.setBeautyLevel(beautyLevel);
            return this;
        }

        public Builder setBeautyStatus(boolean beautyStatus) {
            this.mParam.setBeautyStatus(beautyStatus);
            return this;
        }

        public Builder setCameraType(CameraType cameraType) {
            this.mParam.setCameraType(cameraType);
            return this;
        }

        public Builder setFlashType(FlashType flashType) {
            this.mParam.setFlashType(flashType);
            return this;
        }

        public Builder setMaxDuration(int maxDuration) {
            this.mParam.setMaxDuration(maxDuration);
            return this;
        }

        public Builder setMinDuration(int minDuration) {
            this.mParam.setMinDuration(minDuration);
            return this;
        }

        public Builder setNeedClip(boolean needClip) {
            this.mParam.setNeedClip(needClip);
            return this;
        }

        public Builder setSortMode(int sortMode) {
            this.mParam.setSortMode(sortMode);
            return this;
        }

        public Builder setCropUseGPU(boolean isUseGPU) {
            this.mParam.setCropUseGPU(isUseGPU);
            return this;
        }

        public Builder setVideoCodec(VideoCodecs codec) {
            this.mParam.setVideoCodec(codec);
            return this;
        }

        public JRSnapVideoParam build() {
            return this.mParam;
        }

    }

}
