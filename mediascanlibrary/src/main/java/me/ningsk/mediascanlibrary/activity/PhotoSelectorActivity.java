package me.ningsk.mediascanlibrary.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.adapter.FolderAdapter;
import me.ningsk.mediascanlibrary.adapter.MediaAdapter;
import me.ningsk.mediascanlibrary.docaration.GridSpacingItemDecoration;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.entity.LocalMediaFolder;
import me.ningsk.mediascanlibrary.loader.MediaLoader;
import me.ningsk.mediascanlibrary.permissions.RxPermissions;
import me.ningsk.mediascanlibrary.utils.DataTransferStation;
import me.ningsk.mediascanlibrary.utils.LogUtils;
import me.ningsk.mediascanlibrary.utils.ScreenUtils;
import me.ningsk.mediascanlibrary.utils.ToastManage;
import me.ningsk.mediascanlibrary.widget.FolderPopupWindow;

public class PhotoSelectorActivity extends PhotoBaseActivity implements MediaLoader.MediaCallBack,View.OnClickListener,FolderAdapter.OnItemClickListener {
    private final MediaLoader mMediaLoader = new MediaLoader();
    private RecyclerView mRvList;
    private MediaAdapter mMediaAdapter;
    private FolderPopupWindow mFolderWindow;
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private RxPermissions rxPermissions;
    private TextView photoTitle;
    private TextView photoRight;
    private TextView photoLeft;
    private List<LocalMedia> images = new ArrayList<>();
    private RelativeLayout rlPhotoTitle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rxPermissions = new RxPermissions(this);
        setContentView(R.layout.activity_photo_selector);
        initView(savedInstanceState);
        initListener();
        mMediaLoader.onCreate(this, this);
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

    private void initView(Bundle saveInstanceState) {
        rlPhotoTitle = findViewById(R.id.rl_photo_title);
        photoTitle = findViewById(R.id.photo_title);
        photoTitle.setText(getString(R.string.photo_camera_roll));
        photoTitle.setOnClickListener(this);
        photoLeft = findViewById(R.id.photo_left_back);
        photoLeft.setOnClickListener(this);
        photoRight = findViewById(R.id.photo_right);
        photoRight.setOnClickListener(this);
        mRvList = findViewById(R.id.rv_list);
        mFolderWindow = new FolderPopupWindow(this);
        mFolderWindow.setPhotoTitleView(photoTitle);
        mFolderWindow.setOnItemClickListener(this);
        mRvList.setHasFixedSize(true);
        mRvList.addItemDecoration(new GridSpacingItemDecoration(mOptions.gridSize,
                ScreenUtils.dip2px(this, 2), false));
        mRvList.setLayoutManager(new GridLayoutManager(this, mOptions.gridSize));
        mMediaAdapter = new MediaAdapter(mContext, mOptions);
        mRvList.setAdapter(mMediaAdapter);
    };

    private void initListener() {
       mRvList = findViewById(R.id.rv_list);
    }

    private void readLocalMedia() {
        mMediaLoader.loadMedia();
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
                closeActivity();
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

    }

    @Override
    public void onItemClick(String folderName, List<LocalMedia> medias) {
        photoTitle.setText(folderName);
        mMediaAdapter.bindImagesData(medias);
        mFolderWindow.dismiss();
    }



}
