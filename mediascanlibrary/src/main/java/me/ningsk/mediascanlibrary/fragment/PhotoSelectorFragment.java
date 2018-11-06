package me.ningsk.mediascanlibrary.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.activity.PhotoSelectorActivity;
import me.ningsk.mediascanlibrary.adapter.FolderAdapter;
import me.ningsk.mediascanlibrary.adapter.MediaAdapter;
import me.ningsk.mediascanlibrary.config.PhotoSelectorConfig;
import me.ningsk.mediascanlibrary.config.SelectionOptions;
import me.ningsk.mediascanlibrary.dialog.PhotoDialog;
import me.ningsk.mediascanlibrary.docaration.GridSpacingItemDecoration;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.entity.LocalMediaFolder;
import me.ningsk.mediascanlibrary.loader.MediaLoader;
import me.ningsk.mediascanlibrary.permissions.RxPermissions;
import me.ningsk.mediascanlibrary.utils.ScreenUtils;
import me.ningsk.mediascanlibrary.utils.ToastManage;
import me.ningsk.mediascanlibrary.widget.CoordinatorLinearLayout;
import me.ningsk.mediascanlibrary.widget.CoordinatorRecyclerView;
import me.ningsk.mediascanlibrary.widget.FolderPopupWindow;
import me.ningsk.mediascanlibrary.widget.cropper.nocropper.CropperView;

/**
 * <p>描述：媒体选择、裁剪<p>
 * 作者：ningsk<br>
 * 日期：2018/11/1 15 01<br>
 * 版本：v1.0<br>
 */
public class PhotoSelectorFragment extends Fragment implements MediaLoader.MediaCallBack,View.OnClickListener,FolderAdapter.OnItemClickListener,MediaAdapter.OnPhotoSelectChangedListener {
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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DIALOG:
                    showPleaseDialog();
                    break;
                case DISMISS_DIALOG:
                    dismissDialog();
                    break;
            }
        }
    };

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
        mMediaAdapter.setOnPhotoSelectChangedListener (this);
        mRvList.setAdapter(mMediaAdapter);
    };


    private void readLocalMedia() {
        mMediaLoader.loadMedia();
    }

    private void setLayoutSize() {
        int topViewHeight = ScreenUtils.dip2px(mContext,TOP_REMAIN_HEIGHT) + ScreenUtils.getScreenWidth(mContext);
        final int topBarHeight = ScreenUtils.dip2px(mContext,TOP_REMAIN_HEIGHT);
        parentLayout.setTopViewParam(topViewHeight, topBarHeight);
        layoutCrop.post(new Runnable() {
            @Override
            public void run() {
                layoutCrop.getLayoutParams().height = ScreenUtils.getScreenWidth(mContext);
            }
        });
        mRvList.post(new Runnable() {
            @Override
            public void run() {
                        mRvList.getLayoutParams().height = ScreenUtils.getScreenHeight(mContext) - topBarHeight;
            }
        });
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
                ((PhotoSelectorActivity)mContext).closeActivity();
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
            cropperView.snapImage();
        }
        if (id == R.id.rotation_button) {
            cropperView.rotateImageInner(ROTATION_DEGREE, cropperView.isFillMode());
        }
        if (id == R.id.multi_button) {
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
        if (!((PhotoSelectorActivity)mContext).isFinishing()) {
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

    @Override
    public void onPictureClick(LocalMedia media, int position) {
        if (isGo) {
            cropperView.loadNewImage(media.getPath(), 0);
        } else {
            cropperView.addMultiImage(media.getPath(), 0);
        }

    }
}
