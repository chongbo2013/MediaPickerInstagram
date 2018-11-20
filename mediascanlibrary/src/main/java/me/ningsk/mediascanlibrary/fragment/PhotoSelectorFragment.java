package me.ningsk.mediascanlibrary.fragment;

import android.Manifest;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.activity.PhotoSelectorActivity;
import me.ningsk.mediascanlibrary.adapter.FolderAdapter;
import me.ningsk.mediascanlibrary.adapter.MediaAdapter;
import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.config.PhotoSelectorConfig;
import me.ningsk.mediascanlibrary.config.SelectionOptions;
import me.ningsk.mediascanlibrary.dialog.PhotoDialog;
import me.ningsk.mediascanlibrary.docaration.GridSpacingItemDecoration;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.entity.LocalMediaFolder;
import me.ningsk.mediascanlibrary.entity.VideoDisplayMode;
import me.ningsk.mediascanlibrary.loader.MediaLoader;
import me.ningsk.mediascanlibrary.permissions.RxPermissions;
import me.ningsk.mediascanlibrary.utils.ScreenUtils;
import me.ningsk.mediascanlibrary.utils.ToastManage;
import me.ningsk.mediascanlibrary.widget.CoordinatorLinearLayout;
import me.ningsk.mediascanlibrary.widget.CoordinatorRecyclerView;
import me.ningsk.mediascanlibrary.widget.FolderPopupWindow;
import me.ningsk.mediascanlibrary.widget.VideoTrimFrameLayout;
import me.ningsk.mediascanlibrary.widget.cropper.nocropper.CropperView;

/**
 * <p>描述：媒体选择、裁剪<p>
 * 作者：ningsk<br>
 * 日期：2018/11/1 15 01<br>
 * 版本：v1.0<br>
 */
public class PhotoSelectorFragment extends Fragment implements MediaLoader.MediaCallBack, View.OnClickListener, FolderAdapter.OnItemClickListener, Handler.Callback,
        MediaAdapter.OnPhotoSelectChangedListener, TextureView.SurfaceTextureListener, VideoTrimFrameLayout.OnVideoScrollCallBack, MediaPlayer.OnVideoSizeChangedListener {
    private static final String TAG = PhotoSelectorFragment.class.getSimpleName();
    private final MediaLoader mMediaLoader = new MediaLoader();
    private PhotoDialog dialog;
    private Context mContext;
    private static final int TOP_REMAIN_HEIGHT = 48;
    private MediaAdapter mMediaAdapter;
    private FolderPopupWindow mFolderWindow;
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private SelectionOptions mOptions;
    private RxPermissions rxPermissions;
    private TextView photoTitle;
    private TextView photoRight;
    private TextView photoLeft;
    private CoordinatorRecyclerView mRvList;
    private CoordinatorLinearLayout parentLayout;
    private List<LocalMedia> images = new ArrayList<>();
    private RelativeLayout rlPhotoTitle;
    private FrameLayout layoutCrop;
    private CropperView cropperView;
    private ImageView ivSnap;
    private ImageView ivRotate;
    private TextView tvMulti;
    private final static int ROTATION_DEGREE = 90;
    private boolean isGo = true;
    private boolean isVideo = false;
    private Handler mHandler = new Handler(this);

    private VideoTrimFrameLayout frame;
    private TextureView textureView;
    private MediaPlayer mPlayer;
    private Surface mSurface;
    private LocalMedia videoInfo;
    private Handler playHandler = new Handler(this);

    private int currentPlayPos;

    private boolean isPause = false;
    private static final int PLAY_VIDEO = 1000;
    private static final int PAUSE_VIDEO = 1001;
    private static final int END_VIDEO = 1003;

    private int playState = END_VIDEO;
    private long mStartTime;
    private long mEndTime;
    private long videoPos;
    private long lastVideoSeekTime;

    private boolean needPlayStart = false;
    private int frameWidth;
    private int frameHeight;
    private int mScrollX;
    private int mScrollY;
    private int videoWidth;
    private int videoHeight;
    private VideoDisplayMode cropMode = VideoDisplayMode.SCALE;
    public static final VideoDisplayMode SCALE_CROP = VideoDisplayMode.SCALE;
    public static final VideoDisplayMode SCALE_FILL = VideoDisplayMode.FILL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.fragment_photo_selector, container, false);
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mOptions = SelectionOptions.getOptions();
        rxPermissions = new RxPermissions(getActivity());
        initView(view);
        mMediaLoader.onCreate(getActivity(), this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mHandler.sendEmptyMessage(SHOW_DIALOG);
                            readLocalMedia();
                        } else {
                            ToastManage.s(mContext, getString(R.string.photo_jurisdiction));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private void initView(View view) {
        parentLayout = view.findViewById(R.id.parent_layout);
        layoutCrop = view.findViewById(R.id.layout_crop);
        cropperView = view.findViewById(R.id.cropper);
        cropperView.setFillMode(false);
        frame = view.findViewById(R.id.surface_layout);
        textureView = view.findViewById(R.id.video_textureview);
        textureView.setSurfaceTextureListener(this);
        ivSnap = view.findViewById(R.id.snap_button);
        ivSnap.setOnClickListener(this);
        ivRotate = view.findViewById(R.id.rotation_button);
        ivRotate.setOnClickListener(this);
        tvMulti = view.findViewById(R.id.multi_button);
        tvMulti.setOnClickListener(this);
        rlPhotoTitle = view.findViewById(R.id.rl_photo_title);
        photoTitle = view.findViewById(R.id.photo_title);
        photoTitle.setText(getString(R.string.photo_camera_roll));
        photoTitle.setOnClickListener(this);
        photoLeft = view.findViewById(R.id.photo_left_back);
        photoLeft.setOnClickListener(this);
        photoRight = view.findViewById(R.id.photo_right);
        photoRight.setOnClickListener(this);
        mRvList = view.findViewById(R.id.rv_list);
        setLayoutSize();
        mFolderWindow = new FolderPopupWindow(mContext);
        mFolderWindow.setPhotoTitleView(photoTitle);
        mFolderWindow.setOnItemClickListener(this);
        mRvList.setHasFixedSize(true);
        mRvList.addItemDecoration(new GridSpacingItemDecoration(mOptions.gridSize,
                ScreenUtils.dip2px(mContext, 1), false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, mOptions.gridSize);
        mRvList.setLayoutManager(gridLayoutManager);
        mMediaAdapter = new MediaAdapter(mContext, mOptions);
        mMediaAdapter.setOnPhotoSelectChangedListener(this);
        mRvList.setAdapter(mMediaAdapter);
    }

    ;


    private void readLocalMedia() {
        mMediaLoader.loadMedia();
    }

    private void setLayoutSize() {
        int topViewHeight = ScreenUtils.dip2px(mContext, TOP_REMAIN_HEIGHT) + ScreenUtils.getScreenWidth(mContext);
        final int topBarHeight = ScreenUtils.dip2px(mContext, TOP_REMAIN_HEIGHT);
        parentLayout.setTopViewParam(topViewHeight, topBarHeight);
        layoutCrop.post(() -> layoutCrop.getLayoutParams().height = ScreenUtils.getScreenWidth(mContext));
        mRvList.post(() -> mRvList.getLayoutParams().height = ScreenUtils.getScreenHeight(mContext) - topBarHeight);
        parentLayout.getLayoutParams().height = topViewHeight + ScreenUtils.getScreenWidth(mContext) - topBarHeight;
        mRvList.setCoordinatorListener(parentLayout);
    }


    @Override
    public void onLoadFinished(ArrayList<LocalMedia> mediaList, ArrayList<LocalMediaFolder> folderList) {
        if (folderList.size() > 0) {
            LocalMediaFolder folder = folderList.get(0);
            folder.setChecked(true);
            List<LocalMedia> localImg = mediaList;
            // 这里解决有些机型会出现拍照完，相册列表不及时刷新问题
            // 因为onActivityResult里手动添加拍照后的照片，
            // 如果查询出来的图片大于或等于当前adapter集合的图片则取更新后的，否则就取本地的
            if (localImg.size() >= images.size()) {
                images = localImg;
                mFolderWindow.bindFolder(folderList);
            }
        }
        if (mMediaAdapter != null) {
            if (images == null) {
                images = new ArrayList<>();
            }
            mMediaAdapter.bindImagesData(images);
        }
        mHandler.sendEmptyMessage(DISMISS_DIALOG);
    }

    @Override
    public void onLoaderReset() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.photo_left_back) {
            if (mFolderWindow.isShowing()) {
                mFolderWindow.dismiss();
            } else {
                ((PhotoSelectorActivity) mContext).closeActivity();
            }
        }
        if (id == R.id.photo_title) {
            if (mFolderWindow.isShowing()) {
                mFolderWindow.dismiss();
            } else {
                if (images != null && images.size() > 0) {
                    mFolderWindow.showAsDropDown(rlPhotoTitle);
                    List<LocalMedia> selectedImages = mMediaAdapter.getSelectedImages();
                    mFolderWindow.notifyDataCheckedStatus(selectedImages);
                }
            }

        }
        if (id == R.id.snap_button) {
            if (isVideo) {
                if (cropMode == SCALE_FILL) {
                    scaleCrop(videoWidth, videoHeight);
                } else if (cropMode == SCALE_CROP) {
                    scaleFill(videoWidth, videoHeight);
                }
            } else {
                cropperView.snapImage();
            }

        }
        if (id == R.id.rotation_button) {
            cropperView.rotateImageInner(ROTATION_DEGREE, cropperView.isFillMode());
        }
        if (id == R.id.multi_button) {
            if (isVideo) {
                Toast.makeText(mContext, mContext.getString(R.string.photo_ruler), Toast.LENGTH_SHORT).show();
                return;
            }
            isGo = !isGo;
            if (isGo) {
                tvMulti.setSelected(false);
                ivRotate.setVisibility(View.VISIBLE);
                ivSnap.setVisibility(View.VISIBLE);
                mMediaAdapter.setSelectMode(PhotoSelectorConfig.SINGLE);
            } else {
                tvMulti.setSelected(true);
                ivRotate.setVisibility(View.GONE);
                ivSnap.setVisibility(View.GONE);
                mMediaAdapter.setSelectMode(PhotoSelectorConfig.MULTIPLE);
            }
            mMediaAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(String folderName, List<LocalMedia> medias) {
        photoTitle.setText(folderName);
        mMediaAdapter.bindImagesData(medias);
        mFolderWindow.dismiss();
    }

    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        if (!((PhotoSelectorActivity) mContext).isFinishing()) {
            dismissDialog();
            dialog = new PhotoDialog(mContext);
            dialog.show();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRemove(LocalMedia media) {
        cropperView.removeImage(media.getPath());
    }

    @Override
    public void onChange(List<LocalMedia> selectImages) {

    }



    private void resetMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.reset();
        }
    }

    @Override
    public void onPictureClick(LocalMedia media, int position) {
        if (isGo) {
            if (media.getMediaMimeType() == MimeType.VIDEO) {
                isVideo = true;
                videoInfo = media;
                cropperView.resetSelectedImage();
                cropperView.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);
                ivRotate.setVisibility(View.GONE);
                isPause = false;
                resetMediaPlayer();
                try {
                    mPlayer.setDataSource(videoInfo.getPath());
                    mPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                if (isVideo) {
                    resetMediaPlayer();
                    isVideo = false;
                }
                frame.setVisibility(View.GONE);
                ivRotate.setVisibility(View.VISIBLE);
                cropperView.setVisibility(View.VISIBLE);
                cropperView.loadNewImage(media.getPath(), 0);
            }

        } else {
            cropperView.addMultiImage(media.getPath(), 0);
        }

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        frameWidth = frame.getWidth();
        frameHeight = frame.getHeight();
        videoWidth = width;
        videoHeight = height;
        mStartTime = 0;
        mEndTime = videoInfo.getDuration();
        if (cropMode == SCALE_CROP) {
            scaleCrop(width, height);
        } else if (cropMode == SCALE_FILL) {
            scaleFill(width, height);
        }

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mPlayer == null) {
            mSurface = new Surface(surface);
            mPlayer = new MediaPlayer();
            mPlayer.setSurface(mSurface);
            mPlayer.setOnPreparedListener(mp -> {
                if (!isPause) {
                    playVideo();
                    playState = PLAY_VIDEO;
                } else {
                    isPause = false;
                    mPlayer.start();
                    mPlayer.seekTo(currentPlayPos);
                    playHandler.sendEmptyMessageDelayed(PAUSE_VIDEO, 100);
                }
            });
            mPlayer.setOnVideoSizeChangedListener(this);
            mPlayer.setOnCompletionListener(mp -> {
                playVideo();
            });
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (mPlayer == null) {
            mSurface = new Surface(surface);
            mPlayer = new MediaPlayer();
            mPlayer.setSurface(mSurface);
            try {
                mPlayer.setDataSource(videoInfo.getPath());
                mPlayer.setOnPreparedListener(mp -> {
                    if (!isPause) {
                        playVideo();
                    } else {
                        isPause = false;
                        mPlayer.start();
                        mPlayer.seekTo(currentPlayPos);
                        playHandler.sendEmptyMessageDelayed(PAUSE_VIDEO, 100);
                    }
                });
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPlayer.setOnVideoSizeChangedListener(this);
            mPlayer.setOnCompletionListener(mp -> {
                playVideo();
            });

        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mSurface = null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {


    }

    @Override
    public void onVideoScroll(float distanceX, float distanceY) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) textureView.getLayoutParams();
        int width = lp.width;
        int height = lp.height;

        if (width > frameWidth || height > frameHeight) {
            int maxHorizontalScroll = width - frameWidth;
            int maxVerticalScroll = height - frameHeight;
            if (maxHorizontalScroll > 0) {
                maxHorizontalScroll = maxHorizontalScroll / 2;
                mScrollX += distanceX;
                if (mScrollX > maxHorizontalScroll) {
                    mScrollX = maxHorizontalScroll;
                }
                if (mScrollX < -maxHorizontalScroll) {
                    mScrollX = -maxHorizontalScroll;
                }
            }
            if (maxVerticalScroll > 0) {
                maxVerticalScroll = maxVerticalScroll / 2;
                mScrollY += distanceY;
                if (mScrollY > maxVerticalScroll) {
                    mScrollY = maxHorizontalScroll;
                }
                if (mScrollY < -maxHorizontalScroll) {
                    mScrollY = -maxVerticalScroll;
                }
            }
            lp.setMargins(0, 0, mScrollX, mScrollY);
        }
        textureView.setLayoutParams(lp);
    }

    @Override
    public void onVideoSingleTapUp() {
        if (playState == END_VIDEO) {
            playVideo();
        } else if (playState == PLAY_VIDEO) {
            pauseVideo();
            playState = PAUSE_VIDEO;
        } else if (playState == PAUSE_VIDEO) {
            resumeVideo();
            playState = PLAY_VIDEO;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG:
                showPleaseDialog();
                break;
            case DISMISS_DIALOG:
                dismissDialog();
                break;
            case PAUSE_VIDEO:
                pauseVideo();
                playState = PAUSE_VIDEO;
                break;
            case PLAY_VIDEO:
                if (mPlayer != null) {
                    currentPlayPos = (int) (videoPos + System.currentTimeMillis() - lastVideoSeekTime);
                    Log.d(TAG, "currentPlayPos: " + currentPlayPos);
                    if (currentPlayPos < mEndTime) {
                        playHandler.sendEmptyMessageDelayed(PLAY_VIDEO, 100);
                    } else {
                        playVideo();
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void playVideo() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.seekTo((int) mStartTime);
        mPlayer.start();
        videoPos = mStartTime;
        lastVideoSeekTime = System.currentTimeMillis();
        playHandler.sendEmptyMessage(PLAY_VIDEO);
        // 重新播放之后修改为false，防止暂停、播放的时候重新开始播放。
        needPlayStart = false;
    }

    private void pauseVideo() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.pause();
        playHandler.removeMessages(PLAY_VIDEO);
    }

    private void resumeVideo() {
        if (mPlayer == null) {
            return;
        }
        if (needPlayStart) {
            playVideo();
            needPlayStart = false;
            return;
        }
        lastVideoSeekTime = System.currentTimeMillis() + videoPos - currentPlayPos;
        mPlayer.start();
        playHandler.sendEmptyMessage(PLAY_VIDEO);
    }

    private void resetScroll() {
        mScrollX = 0;
        mScrollY = 0;
    }

    private void scaleFill(int videoWidth, int videoHeight) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textureView.getLayoutParams();
        int s = Math.min(videoWidth, videoHeight);
        int b = Math.max(videoWidth, videoHeight);
        float videoRatio = (float) b / s;
        float ratio = 1f;
        if (videoWidth > videoHeight) {
            layoutParams.width = frameWidth;
            layoutParams.height = frameWidth * videoHeight / videoWidth;
        } else {
            if (videoRatio >= ratio) {
                layoutParams.height = frameHeight;
                layoutParams.width = frameHeight * videoWidth / videoHeight;
            } else {
                layoutParams.width = frameWidth;
                layoutParams.height = frameWidth * videoHeight / videoWidth;
            }
        }
        layoutParams.setMargins(0, 0, 0, 0);
        textureView.setLayoutParams(layoutParams);
        cropMode = SCALE_FILL;
        resetScroll();
    }

    private void scaleCrop(int videoWidth, int videoHeight) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textureView.getLayoutParams();
        int s = Math.min(videoWidth, videoHeight);
        int b = Math.max(videoWidth, videoHeight);
        float videoRatio = (float) b / s;
        float ratio = 1f;
        if (videoWidth > videoHeight) {
            layoutParams.height = frameHeight;
            layoutParams.width = frameHeight * videoWidth / videoHeight;
        } else {
            if (videoRatio >= ratio) {
                layoutParams.width = frameWidth;
                layoutParams.height = frameWidth * videoHeight / videoWidth;
            } else {
                layoutParams.height = frameHeight;
                layoutParams.width = frameHeight * videoWidth / videoHeight;

            }
        }
        layoutParams.setMargins(0, 0, 0, 0);
        textureView.setLayoutParams(layoutParams);
        cropMode = SCALE_CROP;
        resetScroll();
    }

}
