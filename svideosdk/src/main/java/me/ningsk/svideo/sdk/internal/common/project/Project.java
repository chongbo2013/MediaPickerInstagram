package me.ningsk.svideo.sdk.internal.common.project;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.ningsk.svideo.sdk.external.struct.CanvasInfo;
import me.ningsk.svideo.sdk.external.struct.PasterDescriptor;
import me.ningsk.svideo.sdk.external.struct.common.TailWatermark;
import me.ningsk.svideo.sdk.external.struct.effect.ActionBase;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 28<br>
 * 版本：v1.0<br>
 */
public class Project
{
    public static final int FLAG_BIT_DIY_OVERLAY = 1;
    public static final int FLAG_BIT_MV = 2;
    public static final int FLAG_BIT_COLOR_FILTER = 4;
    public static final int FLAG_BIT_WATER_MARK = 8;
    public static final int FLAG_BIT_AUDIO_MIX = 16;
    public static final int FLAG_BIT_KERNEL_FRAME = 32;
    public static final int GENERATE_MODE_FULL = 7;
    public static final int GENERATE_MODE_FULL_ADD_WATERMARK = 15;
    public static final int GENERATE_MODE_DIY_ANIMATION = 5;
    public static final int GENERATE_MODE_WITHOUT_OVERLAY = 6;
    public static final int GENERATE_MODE_WITHOUT_MV = 5;
    public static final int GENERATE_MODE_WITHOUT_FILTER = 3;
    public static final int GENERATE_MODE_RECORD_PREVIEW = 0;
    public static final int CURRENT_LAYOUT_VERSION = 2;
    public static final String PROJECT_SUFFIX = ".QuProj";
    public static final String PROJECT_FILENAME_VER2 = "project.json";
    public static final int PROJECT_VIDEO_HEIGHT = 480;
    public static final int PROJECT_VIDEO_WIDTH = 480;
    public static final int PROJECT_PHOTO_HEIGHT = 720;
    public static final int PROJECT_PHOTO_WIDTH = 720;
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_PHOTO = 1;
    public static final String TRACK_ID_DUBBING = "dubbing";
    public static final String TRACK_ID_PRIMARY = "primary";
    private int mLayoutVersion = 1;
    private String mRequestID = null;
    private List<Filter> mAnimationFilters = new ArrayList();
    private Filter mColorFilter;
    private String mRandomMusic;
    private String mVideoMV;
    private int mMVId;
    private WaterMark waterMark;
    private TailWatermark tailWatermark;
    private int mOutputWidth = 0;
    private int mOutputHeight = 0;
    private int mProjectVersion;
    private File mProjectDir;
    private File mProjectFile;
    private long mLastModified = -1L;
    private int mAudioMixVolume = 100;
    private int mPrimaryVolume = 100;

    private boolean mGeneratePreview = true;
    private CanvasInfo canvasInfo;
    private String canvasPath;
    private int type;
    private int mGop = -1;
    private int mVideoQuality = -1;
    private int mBps;
    private int mFps;
    private int mScaleMode;
    private int mVideoCodec;
    private AudioMix mAudioMix;
    private boolean isSilence;
    private File mRenderOutputFile;
    private final HashMap<String, String> mAudioMixOverride = new HashMap();

    private final HashMap<String, Float> mAudioVolumeOverride = new HashMap();

    public final ArrayList<Track> mTrackList = new ArrayList();

    private List<PasterDescriptor> pasterList = new ArrayList();

    private List<StaticImage> mStaticImages = new ArrayList();

    public int getLayoutVersion()
    {
        return this.mLayoutVersion;
    }

    public int getProjectVersion()
    {
        return this.mProjectVersion;
    }

    public void setProjectVersion(int version) {
        this.mProjectVersion = version;
    }
    public int getCanvasWidth() {
        return this.mOutputWidth; }
    public int getCanvasHeight() { return this.mOutputHeight; }

    public void setCanvasSize(int width, int height) {
        this.mOutputWidth = width;
        this.mOutputHeight = height;
    }

    public File getProjectDir()
    {
        return this.mProjectDir;
    }

    public void setProjectDir(File dir, File file) {
        this.mProjectDir = dir;
        this.mProjectFile = file;
        this.mLayoutVersion = 2;
    }

    public File getProjectFile()
    {
        return this.mProjectFile;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setGop(int gop) {
        this.mGop = gop;
    }

    public int getGop() {
        return this.mGop;
    }

    public int getVideoQuality() {
        return this.mVideoQuality;
    }

    public void setVideoQuality(int videoQuality) {
        this.mVideoQuality = videoQuality;
    }

    public int getVideoCodec() {
        return this.mVideoCodec;
    }

    public void setVideoCodec(int videoCodec) {
        this.mVideoCodec = videoCodec;
    }

    public int getFps() {
        return this.mFps;
    }

    public void setFps(int fps) {
        this.mFps = fps;
    }

    public void setBps(int bps) {
        this.mBps = bps;
    }

    public int getBps() {
        return this.mBps;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public boolean isSilence()
    {
        return this.isSilence;
    }

    public void setSilence(boolean silence) {
        this.isSilence = silence;
    }

    public AudioMix getAudioMix() {
        return this.mAudioMix;
    }

    public void setAudioMix(AudioMix mAudioMix) {
        this.mAudioMix = mAudioMix;
    }

    public int getAudioId() {
        return this.mAudioMix == null ? 0 : this.mAudioMix.id;
    }

    public File getRenderOutputFile()
    {
        return this.mRenderOutputFile;
    }

    public void setRenderOutputFile(File file) {
        this.mRenderOutputFile = file;
    }

    public boolean hasRenderOutput() {
        if (this.mRenderOutputFile == null) {
            return false;
        }
        return true;
    }
    public boolean isEmpty() {
        return getPrimaryTrack().isEmpty();
    }

    public boolean validate() {
        for (Track track : this.mTrackList) {
            if (!track.validate()) {
                return false;
            }
        }

        return true;
    }

    /** @deprecated */
    public void setClipList(Clip[] videos)
    {
        getPrimaryTrack().setClipArray(videos);
    }

    @Deprecated
    public float getDurationSecond()
    {
        long duration_nano = getDurationNano();
        return TimeUnitUtil.nanosToSeconds(duration_nano);
    }

    public long getDurationNano() {
        long primary_dur = getPrimaryTrack().getDuration();
        if (primary_dur > 0L) {
            return primary_dur;
        }
        return 0L;
    }

    public long getTimestamp()
    {
        return this.mLastModified;
    }

    public void setTimestamp(long timestamp) {
        this.mLastModified = timestamp;
    }

    public void updateModifiedTime() {
        this.mLastModified = System.currentTimeMillis();
    }

    public Uri getUri() {
        return this.mProjectFile == null ? null : Uri.fromFile(this.mProjectFile);
    }

    public int getVersion() {
        return 1;
    }

    public void setColorEffect(Filter filter) {
        this.mColorFilter = filter;
    }

    public Filter getColorEffect() {
        return this.mColorFilter;
    }

    public void setRandomMusic(String music)
    {
        this.mRandomMusic = music;
    }

    public String getRandomMusic() {
        return this.mRandomMusic;
    }

    public int getMVId()
    {
        return this.mMVId;
    }

    public void setMVId(int mMVId) {
        this.mMVId = mMVId;
    }

    public void setVideoMV(String uri) {
        this.mVideoMV = uri;
    }

    public String getVideoMV() {
        return this.mVideoMV;
    }

    public HashMap<String, String> getAudioMixOverride()
    {
        return this.mAudioMixOverride;
    }

    public void setAudioMixOverride(HashMap<String, String> value) {
        this.mAudioMixOverride.clear();
        this.mAudioMixOverride.putAll(value);
    }

    public void addAudioMixOverride(String path) {
        if (this.mVideoMV == null) {
            return;
        }
        this.mAudioMixOverride.put(this.mVideoMV, path);
    }

    public HashMap<String, Float> getAudioVolumeOverride()
    {
        return this.mAudioVolumeOverride;
    }

    public void setAudioVolumeOverride(HashMap<String, Float> value) {
        this.mAudioVolumeOverride.clear();
        this.mAudioVolumeOverride.putAll(value);
    }

    public void addAudioVolumeOverride(float value) {
        if (this.mVideoMV == null) {
            return;
        }

        this.mAudioVolumeOverride.put(this.mVideoMV, Float.valueOf(value));
    }

    public void setAudioMixVolume(int value)
    {
        this.mAudioMixVolume = value;
    }

    public int getAudioMixVolume() {
        return this.mAudioMixVolume;
    }

    public void setPrimaryAudioVolume(int value)
    {
        this.mPrimaryVolume = value;
    }

    public int getPrimaryAudioVolume() {
        return this.mPrimaryVolume;
    }

    public float getResolvedPrimaryAudioVolume()
    {
        Track track = getTrackByID("dubbing");
        if ((track != null) && (!track.isEmpty())) {
            return 1.0F - track.getVolume();
        }

        if (this.mVideoMV != null) {
            Float weight = (Float)this.mAudioVolumeOverride.get(this.mVideoMV);
            return weight != null ? weight.floatValue() : 0.3F;
        }

        if (this.mAudioMix != null) {
            return 1 - this.mAudioMixVolume;
        }

        return this.mPrimaryVolume;
    }

    public void setGeneratePreview(boolean value)
    {
        this.mGeneratePreview = value;
    }

    public boolean getGeneratePreview() {
        return this.mGeneratePreview;
    }

    public static File getProjectFile(File dir) {
        File file = new File(dir, "project.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void setTrackArray(Track[] track_list)
    {
        for (Track track : track_list)
            addTrack(track);
    }

    public Track[] getTrackArray() {
        return (Track[])this.mTrackList.toArray(new Track[0]);
    }

    @Nullable
    public Track getTrackByID(@NonNull String id) {
        for (Track track : this.mTrackList) {
            if (id.equals(track.id)) {
                return track;
            }
        }
        return null;
    }

    @NonNull
    public Track getPrimaryTrack() {
        return findOrCreateTrack("primary");
    }
    @NonNull
    public Track findOrCreateTrack(@NonNull String id) {
        Track track = getTrackByID(id);
        if (track == null) {
            track = new Track();
            track.id = id;
            this.mTrackList.add(track);
        }

        return track;
    }

    public int getTrackIndex(@NonNull String id) {
        int i = 0; for (int count = this.mTrackList.size(); i < count; i++) {
            if (id.equals(((Track)this.mTrackList.get(i)).id)) {
                return i;
            }
        }
        return -1;
    }

    public Track addTrack(@NonNull Track track) {
        int i = getTrackIndex(track.id);
        if (i < 0) {
            this.mTrackList.add(track);
            return null;
        }
        return (Track)this.mTrackList.set(i, track);
    }

    public Track removeTrack(String id)
    {
        int i = 0; for (int count = this.mTrackList.size(); i < count; i++) {
        if (id.equals(((Track)this.mTrackList.get(i)).id)) {
            return (Track)this.mTrackList.remove(i);
        }
    }
        return null;
    }

    public List<PasterDescriptor> getPasterList()
    {
        return this.pasterList;
    }

    public void setPasterList(List<PasterDescriptor> pasterList) {
        this.pasterList.clear();
        this.pasterList.addAll(pasterList);
    }

    public List<StaticImage> getStaticImages()
    {
        return this.mStaticImages;
    }

    public void setStaticImages(List<StaticImage> staticImages) {
        this.mStaticImages.clear();
        this.mStaticImages.addAll(staticImages);
    }

    public WaterMark getWaterMark() {
        return this.waterMark;
    }
    public void setWaterMark(WaterMark waterMark) {
        this.waterMark = waterMark;
    }

    public TailWatermark getTailWatermark()
    {
        return this.tailWatermark;
    }

    public void setTailWatermark(TailWatermark tailWatermark) {
        this.tailWatermark = tailWatermark;
    }

    public CanvasInfo getCanvasInfo()
    {
        return this.canvasInfo;
    }

    public void setCanvasInfo(CanvasInfo canvasInfo) {
        this.canvasInfo = canvasInfo;
    }

    public String getCanvasPath() {
        return this.canvasPath;
    }

    public void setCanvasPath(String canvasPath) {
        this.canvasPath = canvasPath;
    }

    public String getRequestID() {
        return this.mRequestID;
    }

    public void setRequestID(String requestID) {
        this.mRequestID = requestID;
    }

    public void addAnimationFilter(Filter filter)
    {
        if (filter != null)
            this.mAnimationFilters.add(filter);
    }

    public boolean removeAnimationFilter(Filter fi)
    {
        return this.mAnimationFilters.remove(fi);
    }

    public void clearAnimationFilters() {
        this.mAnimationFilters.clear();
    }

    public Collection<Filter> getAllAnimationFilters() {
        return this.mAnimationFilters;
    }
}