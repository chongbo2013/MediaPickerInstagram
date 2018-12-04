package com.jerei.mediapickerinstagram;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import me.ningsk.cameralibrary.activity.CameraActivity;
import me.ningsk.utilslibrary.utils.PermissionUtils;
import me.ningsk.cameralibrary.engine.PreviewEngine;
import me.ningsk.cameralibrary.engine.model.AspectRatio;
import me.ningsk.cameralibrary.engine.model.GalleryType;
import me.ningsk.cameralibrary.listener.OnGallerySelectedListener;
import me.ningsk.cameralibrary.listener.OnPreviewCaptureListener;
import me.ningsk.filterlibrary.glfilter.resource.FilterHelper;
import me.ningsk.filterlibrary.glfilter.resource.ResourceHelper;
import me.ningsk.imagelibrary.activity.ImageEditActivity;
import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.config.PhotoSelectorConfig;
import me.ningsk.mediascanlibrary.engine.MediaScanEngine;
import me.ningsk.videolibrary.activity.VideoEditActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0;
    private Button mBtnCamera;
    private Button mBtnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initView();
        initResources();
    }

    private void checkPermissions() {
        boolean cameraEnable = PermissionUtils.permissionChecking(this,
                Manifest.permission.CAMERA);
        boolean storageWriteEnable = PermissionUtils.permissionChecking(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean recordAudio = PermissionUtils.permissionChecking(this,
                Manifest.permission.RECORD_AUDIO);
        if (!cameraEnable || !storageWriteEnable || !recordAudio) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                    }, REQUEST_CODE);
        }
    }

    private void initView() {
        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        mBtnCamera.setOnClickListener(this);
        mBtnEdit = (Button) findViewById(R.id.btn_edit);
        mBtnEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera: {
                previewCamera();
                break;
            }

            case R.id.btn_edit: {
                scanMedia();
                break;
            }
        }
    }

    /**
     * 初始化动态贴纸、滤镜等资源
     */
    private void initResources() {
        new Thread(() -> {
            ResourceHelper.initAssetsResource(MainActivity.this);
            FilterHelper.initAssetsFilter(MainActivity.this);
        }).start();
    }

    /**
     * 打开预览页面
     */
    private void previewCamera() {
        PreviewEngine.from(this)
                .setCameraRatio(AspectRatio.RATIO_1_1)
                .showFacePoints(false)
                .showFps(false)
                .setGalleryListener(type -> scanMedia())
                .setPreviewCaptureListener((path, type) -> {
                    if (type == GalleryType.PICTURE) {
                        Intent intent = new Intent(MainActivity.this, ImageEditActivity.class);
                        intent.putExtra(ImageEditActivity.PATH, path);
                        startActivity(intent);
                    } else if (type == GalleryType.VIDEO) {
                        Intent intent = new Intent(MainActivity.this, VideoEditActivity.class);
                        intent.putExtra(VideoEditActivity.PATH, path);
                        startActivity(intent);
                    }
                })
                .startPreview();
    }

    /**
     * 扫描媒体库
     */
    private void scanMedia() {
        MediaScanEngine.from(this)
                .openGallery(MimeType.ALL)
                .theme(me.ningsk.mediascanlibrary.R.style.photo_ins_style)
                .imageSpanCount(4)
                .maxSelectNum(8)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .selectionMode(PhotoSelectorConfig.SINGLE)
                .isZoomAnim(false)
                .forResult(PhotoSelectorConfig.CHOOSE_REQUEST, CameraActivity.class);
    }
}
