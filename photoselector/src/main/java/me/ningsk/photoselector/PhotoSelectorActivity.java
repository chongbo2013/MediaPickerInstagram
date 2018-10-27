package me.ningsk.photoselector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.ningsk.photoselector.adapter.FolderAdapter;
import me.ningsk.photoselector.adapter.MediaAdapter;
import me.ningsk.photoselector.bean.FolderBean;
import me.ningsk.photoselector.bean.MediaBean;
import me.ningsk.photoselector.compress.CompressConfig;
import me.ningsk.photoselector.config.PhotoSelectorConfig;
import me.ningsk.photoselector.dialog.PhotoDialog;
import me.ningsk.photoselector.docaration.GridSpacingItemDecoration;
import me.ningsk.photoselector.loader.MediaLoader;
import me.ningsk.photoselector.permissions.RxPermissions;
import me.ningsk.photoselector.utils.DataTransferStation;
import me.ningsk.photoselector.utils.LogUtils;
import me.ningsk.photoselector.utils.ScreenUtils;
import me.ningsk.photoselector.utils.StatusBarUtils;
import me.ningsk.photoselector.utils.ToastManage;
import me.ningsk.photoselector.widget.FolderPopupWindow;

public class PhotoSelectorActivity extends AppCompatActivity implements MediaLoader.MediaCallBack, AdapterView.OnItemClickListener,View.OnClickListener,FolderAdapter.OnItemClickListener {
    private final static String TAG = PhotoSelectorActivity.class.getSimpleName();
    private Context mContext;
    private SelectionOptions mOptions;
    private final MediaLoader mMediaLoader = new MediaLoader();
    private RecyclerView mRvList;
    private MediaAdapter mMediaAdapter;
    private FolderPopupWindow mFolderWindow;
    private PhotoDialog dialog;
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private DataTransferStation mDataTransferStation;
    private RxPermissions rxPermissions;
    private RelativeLayout rlPhotoTitle;
    private TextView photoTitle;
    private TextView photoRight;
    private ImageView photoLeft;
    // 是否重新加载媒体库
    private boolean mRestartLoadMedia;
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
        mOptions = SelectionOptions.getOptions();
        setTheme(mOptions.themeId);
        super.onCreate(savedInstanceState);
        mContext = this;
        rxPermissions = new RxPermissions(this);
        setContentView(R.layout.activity_photo_selector);
        StatusBarUtils.immersiveStatusBar(this);
        initView(savedInstanceState);
        initListener();
        mDataTransferStation = DataTransferStation.getInstance();
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
        rlPhotoTitle =  findViewById(R.id.rl_photo_title);
        photoTitle = findViewById(R.id.photo_title);
        photoTitle.setText(getString(R.string.photo_camera_roll));
        photoTitle.setOnClickListener(this);
        photoLeft = findViewById(R.id.photo_left_back);
        photoLeft.setOnClickListener(this);
        photoRight = findViewById(R.id.photo_right);
        photoRight.setOnClickListener(this);
        mRvList = findViewById(R.id.rv_list);
        mFolderWindow = new FolderPopupWindow(this, mOptions.mimeType);
        mFolderWindow.setPhotoTitleView(photoTitle);
        mFolderWindow.setOnItemClickListener(this);
        mRvList.setHasFixedSize(true);
        mRvList.addItemDecoration(new GridSpacingItemDecoration(mOptions.gridSize,
                ScreenUtils.dip2px(this, 2), false));
        mRvList.setLayoutManager(new GridLayoutManager(this, mOptions.gridSize));
        mMediaAdapter = new MediaAdapter(this, mRvList);

    };

    private void initListener() {
       mRvList = findViewById(R.id.rv_list);
    }

    private void readLocalMedia() {
        mMediaLoader.loadMedia();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onLoadFinished(ArrayList<MediaBean> mediaList, ArrayList<FolderBean> folderList) {
        // 将数据存入临时的数据存储点
        mDataTransferStation.putItems(mediaList);
        mDataTransferStation.putSelectedItems(mOptions.selectedItems);
        if (mRestartLoadMedia) {
            if (mediaList != null && !mediaList.isEmpty()) {
                MediaBean media = mediaList.get(0);
                LogUtils.e("刚刚添加的MediaBean:" + media.getPath());
                mDataTransferStation.putSelectedItem(media);
            }
            mRestartLoadMedia = false;
        }
        ArrayList<MediaBean> selectItems = mDataTransferStation.getSelectedItems();
        mMediaAdapter.bindImagesData(mediaList);
        mMediaAdapter.bindSelectImages(selectItems);

    }

    @Override
    public void onLoaderReset() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(String folderName, List<MediaBean> medias) {

    }

    protected void showPleaseDialog() {
        if (!isFinishing()) {
            dismissDialog();
            dialog = new PhotoDialog(this);
            dialog.show();
        }
    }

    protected void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onResult(ArrayList<MediaBean> selectItems) {

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mMediaLoader.onDestroy();
        dismissDialog();
        if (mDataTransferStation != null) {
            mDataTransferStation.onDestroy();
            mDataTransferStation = null;
        }
        super.onDestroy();
    }
}
