package me.ningsk.imagelibrary.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;

import me.ningsk.utilslibrary.fragment.BackPressedDialogFragment;
import me.ningsk.utilslibrary.fragment.PermissionConfirmDialogFragment;
import me.ningsk.utilslibrary.fragment.PermissionErrorDialogFragment;
import me.ningsk.utilslibrary.utils.BitmapUtils;
import me.ningsk.utilslibrary.utils.PermissionUtils;
import me.ningsk.imagelibrary.R;


/**
 * <p>描述：图片编辑界面<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 18 10<br>
 * 版本：v1.0<br>
 */
public class ImageEditedFragment extends Fragment {

    private static final String TAG = "ImageEditedFragment";
    private static final boolean VERBOSE = true;

    private static final String FRAGMENT_DIALOG = "dialog";

    // 存储权限使能标志
    private boolean mStorageWriteEnable = false;

    // Fragment主页面
    private View mContentView;

    // 滤镜页面
    private ImageFilterFragment mImageFilterFragment;

    // 布局加载器
    private LayoutInflater mInflater;
    private Activity mActivity;

    // 图片路径
    private String mImagePath;
    private Bitmap mBitmap;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mStorageWriteEnable = PermissionUtils.permissionChecking(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mInflater = LayoutInflater.from(mActivity);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_image_edit, container, false);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mStorageWriteEnable) {
            initView(mContentView);
        } else {
            requestStoragePermission();
        }
    }

    /**
     * 初始化页面
     *
     * @param view
     */
    private void initView(View view) {
        showFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContentView = null;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public void onBackPressed() {
        new BackPressedDialogFragment().show(getChildFragmentManager(), "");
    }


    /**
     * 显示编辑页面
     */
    private void showFragment() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (mImageFilterFragment == null) {
            mImageFilterFragment = new ImageFilterFragment();
            ft.add(R.id.fragment_container, mImageFilterFragment);
        } else {
            ft.show(mImageFilterFragment);
        }
        mImageFilterFragment.showGLSurfaceView(true);
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mImageFilterFragment.setBitmap(mBitmap);
        }
        ft.commit();
    }


    /**
     * 设置图片路径
     *
     * @param path
     */
    public void setImagePath(String path) {
        mImagePath = path;
        mBitmap = BitmapUtils.getBitmapFromFile(new File(mImagePath), 0, 0, true);
    }

    /**
     * 请求存储权限
     */
    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionConfirmDialogFragment.newInstance(getString(R.string.request_storage_permission), PermissionUtils.REQUEST_STORAGE_PERMISSION)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PermissionUtils.REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionErrorDialogFragment.newInstance(getString(R.string.request_storage_permission), PermissionUtils.REQUEST_STORAGE_PERMISSION, true)
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            } else {
                mStorageWriteEnable = true;
                initView(mContentView);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
