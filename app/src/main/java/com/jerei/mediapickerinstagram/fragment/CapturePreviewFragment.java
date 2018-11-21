package com.jerei.mediapickerinstagram.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jerei.mediapickerinstagram.R;

import java.nio.ByteBuffer;
import java.util.List;

import me.ningsk.cameralibrary.engine.camera.CameraEngine;
import me.ningsk.cameralibrary.engine.camera.CameraParam;
import me.ningsk.cameralibrary.engine.listener.OnCameraCallback;
import me.ningsk.cameralibrary.engine.listener.OnCaptureListener;
import me.ningsk.cameralibrary.engine.model.AspectRatio;
import me.ningsk.cameralibrary.engine.render.PreviewRecorder;
import me.ningsk.cameralibrary.engine.render.PreviewRenderer;
import me.ningsk.cameralibrary.listener.OnPageOperationListener;
import me.ningsk.cameralibrary.utils.PathConstraints;
import me.ningsk.cameralibrary.widget.AspectFrameLayout;
import me.ningsk.cameralibrary.widget.JRSurfaceView;
import me.ningsk.cameralibrary.widget.ShutterButton;
import me.ningsk.utilslibrary.fragment.PermissionConfirmDialogFragment;
import me.ningsk.utilslibrary.fragment.PermissionErrorDialogFragment;
import me.ningsk.utilslibrary.utils.BitmapUtils;
import me.ningsk.utilslibrary.utils.BrightnessUtils;
import me.ningsk.utilslibrary.utils.PermissionUtils;

public class CapturePreviewFragment extends Fragment implements View.OnClickListener {

    private static final String FRAGMENT_DIALOG = "dialog";

    // 对焦大小
    private static final int FocusSize = 100;

    // 相机权限使能标志
    private boolean mCameraEnable = false;
    // 存储权限使能标志
    private boolean mStorageWriteEnable = false;
    // 是否需要等待录制完成再跳转
    private boolean mNeedToWaitStop = false;
    // 处于延时拍照状态
    private boolean mDelayTaking = false;

    // 预览参数
    private CameraParam mCameraParam;

    // Fragment主页面
    private View mContentView;
    // 预览部分
    private AspectFrameLayout mAspectLayout;
    private JRSurfaceView mCameraSurfaceView;

    private Button mBtnSwitch;
    private Button mBtnGallery;



    // 快门按钮
    private ShutterButton mBtnShutter;


    // 主线程Handler
    private Handler mMainHandler;
    // 持有该Fragment的Activity，onAttach/onDetach中绑定/解绑，主要用于解决getActivity() = null的情况
    private Activity mActivity;
    // 页面跳转监听器
    private OnPageOperationListener mPageListener;

    public CapturePreviewFragment() {
        mCameraParam = CameraParam.getInstance();
        mCameraParam.setAspectRatio(AspectRatio.RATIO_16_9);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        int currentMode = BrightnessUtils.getSystemBrightnessMode(mActivity);
        if (currentMode == 1) {
            mCameraParam.brightness = -1;
        } else {
            mCameraParam.brightness = BrightnessUtils.getSystemBrightness(mActivity);
        }
        mMainHandler = new Handler(context.getMainLooper());
        mCameraEnable = PermissionUtils.permissionChecking(mActivity, Manifest.permission.CAMERA);
        mStorageWriteEnable = PermissionUtils.permissionChecking(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mCameraParam.audioPermitted = PermissionUtils.permissionChecking(mActivity, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化相机渲染引擎
        PreviewRenderer.getInstance()
                .setCameraCallback(mCameraCallback)
                .setCaptureFrameCallback(mCaptureCallback)
                .initRenderer(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_custom_camera, container, false);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCameraEnable) {
            initView(mContentView);
        } else {
            requestCameraPermission();
        }
    }

    /**
     * 初始化页面
     *
     * @param view
     */
    private void initView(View view) {
        mBtnSwitch = view.findViewById(R.id.btn_switch);
        mBtnSwitch.setOnClickListener(this);
        mBtnGallery = view.findViewById(R.id.btn_gallery);
        mBtnGallery.setOnClickListener(this);
        mAspectLayout = view.findViewById(R.id.layout_aspect);
        mCameraSurfaceView = new JRSurfaceView(mActivity);
        mCameraSurfaceView.addMultiClickListener(mMultiClickListener);
        mAspectLayout.addView(mCameraSurfaceView);
        mAspectLayout.requestLayout();
        mAspectLayout.setAspectRatio(mCameraParam.currentRatio);
        mAspectLayout.requestLayout();
        // 绑定需要渲染的SurfaceView
        PreviewRenderer.getInstance().setSurfaceView(mCameraSurfaceView);
        mBtnShutter = view.findViewById(R.id.btn_shutter);
        mBtnShutter.setOnClickListener(this);
        adjustBottomView();
    }

    /**
     * 调整底部视图
     */
    private void adjustBottomView() {
        boolean result = mCameraParam.currentRatio < CameraParam.Ratio_4_3;
        mBtnShutter.setOuterBackgroundColor(result ? me.ningsk.cameralibrary.R.color.shutter_gray_dark : me.ningsk.cameralibrary.R.color.shutter_gray_light);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerHomeReceiver();
        enhancementBrightness();
        mBtnShutter.setEnableOpened(false);
    }

    /**
     * 增强光照
     */
    private void enhancementBrightness() {
        BrightnessUtils.setWindowBrightness(mActivity, mCameraParam.luminousEnhancement
                ? BrightnessUtils.MAX_BRIGHTNESS : mCameraParam.brightness);
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegisterHomeReceiver();
        mBtnShutter.setEnableOpened(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContentView = null;
    }

    @Override
    public void onDestroy() {
        mPageListener = null;
        // 关掉渲染引擎
        PreviewRenderer.getInstance().destroyRenderer();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    /**
     * 处理返回事件
     *
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_gallery) {
            openGallery();
        } else if (i == R.id.btn_switch) {
            switchCamera();
        } else if (i == R.id.btn_shutter) {
            takePicture();
        }
    }


    private void setFlashLight(boolean flashOn) {
        CameraEngine.getInstance().setFlashLight(flashOn);
    }

    /**
     * 切换相机
     */
    private void switchCamera() {
        if (!mCameraEnable) {
            requestCameraPermission();
            return;
        }
        PreviewRenderer.getInstance().switchCamera();
    }


    /**
     * 拍照
     */
    private void takePicture() {
        if (mStorageWriteEnable) {

            if (mCameraParam.takeDelay && !mDelayTaking) {
                mDelayTaking = true;
                mMainHandler.postDelayed(() -> {
                    mDelayTaking = false;
                    PreviewRenderer.getInstance().takePicture();
                }, 3000);
            } else {
                PreviewRenderer.getInstance().takePicture();
            }

        } else {
            requestStoragePermission();
        }
    }


    /**
     * 单双击回调监听
     */
    private JRSurfaceView.OnMultiClickListener mMultiClickListener = new JRSurfaceView.OnMultiClickListener() {

        @Override
        public void onSurfaceSingleClick(final float x, final float y) {

            // 如果处于触屏拍照状态，则直接拍照，不做对焦处理
            if (mCameraParam.touchTake) {
                takePicture();
                return;
            }

            // 判断是否支持对焦模式
            if (CameraEngine.getInstance().getCamera() != null) {
                List<String> focusModes = CameraEngine.getInstance().getCamera()
                        .getParameters().getSupportedFocusModes();
                if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    CameraEngine.getInstance().setFocusArea(CameraEngine.getFocusArea((int) x, (int) y,
                            mCameraSurfaceView.getWidth(), mCameraSurfaceView.getHeight(), FocusSize));
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCameraSurfaceView.showFocusAnimation();
                        }
                    });
                }
            }
        }

        @Override
        public void onSurfaceDoubleClick(float x, float y) {
            switchCamera();
        }

    };


    // ------------------------------------ 拍照回调 ---------------------------------------------
    private OnCaptureListener mCaptureCallback = new OnCaptureListener() {
        @Override
        public void onCapture(final ByteBuffer buffer, final int width, final int height) {
            mMainHandler.post(() -> {
                String filePath = PathConstraints.getImageCachePath(mActivity);
                BitmapUtils.saveBitmap(filePath, buffer, width, height);
                if (mPageListener != null) {
                    mPageListener.onOpenImageEditPage(filePath);
                }
            });
        }
    };

    // ------------------------------------ 预览回调 ---------------------------------------------
    private OnCameraCallback mCameraCallback = new OnCameraCallback() {

        @Override
        public void onCameraOpened() {
        }

        @Override
        public void onPreviewCallback(byte[] data) {
            if (mBtnShutter != null && !mBtnShutter.isEnableOpened()) {
                mBtnShutter.setEnableOpened(true);
            }

            // 请求刷新
            requestRender();
        }


    };

    /**
     * 请求渲染
     */
    private void requestRender() {
        PreviewRenderer.getInstance().requestRender();
    }




    /**
     * 请求相机权限
     */
    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            PermissionConfirmDialogFragment.newInstance(getString(me.ningsk.cameralibrary.R.string.request_camera_permission), PermissionUtils.REQUEST_CAMERA_PERMISSION, true)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PermissionUtils.REQUEST_CAMERA_PERMISSION);
        }
    }

    /**
     * 请求存储权限
     */
    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionConfirmDialogFragment.newInstance(getString(me.ningsk.cameralibrary.R.string.request_storage_permission), PermissionUtils.REQUEST_STORAGE_PERMISSION)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PermissionUtils.REQUEST_STORAGE_PERMISSION);
        }
    }

    /**
     * 请求录音权限
     */
    private void requestRecordSoundPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            PermissionConfirmDialogFragment.newInstance(getString(me.ningsk.cameralibrary.R.string.request_sound_permission), PermissionUtils.REQUEST_SOUND_PERMISSION)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    PermissionUtils.REQUEST_SOUND_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionErrorDialogFragment.newInstance(getString(me.ningsk.cameralibrary.R.string.request_camera_permission), PermissionUtils.REQUEST_CAMERA_PERMISSION, true)
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            } else {
                mCameraEnable = true;
                initView(mContentView);
            }
        } else if (requestCode == PermissionUtils.REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionErrorDialogFragment.newInstance(getString(me.ningsk.cameralibrary.R.string.request_storage_permission), PermissionUtils.REQUEST_STORAGE_PERMISSION)
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            } else {
                mStorageWriteEnable = true;
            }
        } else if (requestCode == PermissionUtils.REQUEST_SOUND_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionErrorDialogFragment.newInstance(getString(me.ningsk.cameralibrary.R.string.request_sound_permission), PermissionUtils.REQUEST_SOUND_PERMISSION)
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            } else {
                mCameraParam.audioPermitted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 注册服务
     */
    private void registerHomeReceiver() {
        if (mActivity != null) {
            IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mActivity.registerReceiver(mHomePressReceiver, homeFilter);
        }
    }

    /**
     * 注销服务
     */
    private void unRegisterHomeReceiver() {
        if (mActivity != null) {
            mActivity.unregisterReceiver(mHomePressReceiver);
        }
    }

    /**
     * Home按键监听服务
     */
    private BroadcastReceiver mHomePressReceiver = new BroadcastReceiver() {
        private final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (TextUtils.isEmpty(reason)) {
                    return;
                }
            }
        }
    };

    /**
     * 打开图库
     */
    private void openGallery() {
        if (mPageListener != null) {
            mPageListener.onOpenGalleryPage();
        }
    }

    /**
     * 设置页面监听器
     *
     * @param listener
     */
    public void setOnPageOperationListener(OnPageOperationListener listener) {
        mPageListener = listener;
    }
}
