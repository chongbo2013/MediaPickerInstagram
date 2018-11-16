package me.ningsk.croplibrary.fragment;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.ningsk.croplibrary.R;
import me.ningsk.croplibrary.model.VideoDisplayMode;
import me.ningsk.croplibrary.model.VideoQuality;
import me.ningsk.croplibrary.widget.SizeChangedNotifier;
import me.ningsk.croplibrary.widget.VideoTrimFrameLayout;

/**
 * <p>描述：视频裁剪界面<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 11 08<br>
 * 版本：v1.0<br>
 */
public class JRVideoCropFragment extends Fragment implements TextureView.SurfaceTextureListener, View.OnClickListener,Handler.Callback,
            VideoTrimFrameLayout.OnVideoScrollCallBack, SizeChangedNotifier.Listener{

    public static final VideoDisplayMode SCALE_CROP = VideoDisplayMode.SCALE;
    public static final VideoDisplayMode SCALE_FILL = VideoDisplayMode.FILL;

    private static final int PLAY_VIDEO = 1000;
    private static final int PAUSE_VIDEO = 1001;
    private static final int END_VIDEO = 1003;

    private int playState = END_VIDEO;
    private static int OUT_STROKE_WIDTH;
    private VideoTrimFrameLayout frame;
    private TextureView textureview;
    private Surface mSurface;

    private View mRootView;
    private MediaPlayer mPlayer;
    private FrameLayout mCropProgressBg;
    private int ratioMode;
    private VideoQuality quality = VideoQuality.HD;

    private int screenWidth;
    private int screenHeight;
    private int frameWidth;
    private int frameHeight;
    private int videoWidth;
    private int videoHeight;
    private int cropDuration = 2000;

    private VideoDisplayMode cropMode = VideoDisplayMode.SCALE;

    private Handler playHandler = new Handler(this);





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.framgent_jrvideo_crop, container, false);
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        initView();
        initSurface();
    }

    private void initView() {

    }

    private void initSurface(){
        frame = mRootView.findViewById(R.id.video_surface_layout);
        frame.setOnSizeChangedListener(this);
        frame.setOnScrollCallBack(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onVideoScroll(float distanceX, float distanceY) {

    }

    @Override
    public void onVideoSingleTapUp() {

    }

    @Override
    public void onSizeChanged(View view, int currentWidth, int currentHeight, int oldWidth, int oldHeight) {

    }
}
