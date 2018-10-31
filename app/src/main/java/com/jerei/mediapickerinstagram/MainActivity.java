package com.jerei.mediapickerinstagram;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.List;

import me.ningsk.baselibrary.utils.PermissionUtils;
import me.ningsk.cameralibrary.engine.PreviewEngine;
import me.ningsk.cameralibrary.engine.listener.OnCaptureListener;
import me.ningsk.cameralibrary.engine.model.AspectRatio;
import me.ningsk.cameralibrary.engine.model.GalleryType;
import me.ningsk.cameralibrary.listener.OnGallerySelectedListener;
import me.ningsk.cameralibrary.listener.OnPreviewCaptureListener;
import me.ningsk.imagelibrary.activity.ImageEditActivity;
import me.ningsk.photoselector.MimeType;
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
                scanMedia(false);
                break;
            }
        }
    }

    /**
     * 打开预览页面
     */
    private void previewCamera() {
        PreviewEngine.from(this)
                .setCameraRatio(AspectRatio.RATIO_16_9)
                .showFacePoints(false)
                .showFps(true)
                .setGalleryListener(new OnGallerySelectedListener() {
                    @Override
                    public void onGalleryClickListener(GalleryType type) {
                        scanMedia(type == GalleryType.ALL);
                    }
                })
                .setPreviewCaptureListener(new OnPreviewCaptureListener() {
                    @Override
                    public void onMediaSelectedListener(String path, GalleryType type) {
                        if (type == GalleryType.PICTURE) {
                            Intent intent = new Intent(MainActivity.this, ImageEditActivity.class);
                            intent.putExtra(ImageEditActivity.PATH, path);
                            startActivity(intent);
                        } else if (type == GalleryType.VIDEO) {
                            Intent intent = new Intent(MainActivity.this, VideoEditActivity.class);
                            intent.putExtra(VideoEditActivity.PATH, path);
                            startActivity(intent);
                        }
                    }
                })
                .startPreview();
    }

    /**
     * 扫描媒体库
     */
    private void scanMedia(boolean enableGif) {
//        MediaScanEngine.from(this)
//                .setMimeTypes(MimeType.ofAll())
//                .ImageLoader(new GlideMediaLoader())
//                .spanCount(4)
//                .showCapture(true)
//                .enableSelectGif(enableGif)
//                .setCaptureListener(new OnCaptureListener() {
//                    @Override
//                    public void onCapture() {
//                        previewCamera();
//                    }
//                })
//                .setMediaSelectedListener(new OnMediaSelectedListener() {
//                    @Override
//                    public void onSelected(List<Uri> uriList, List<String> pathList, boolean isVideo) {
//                        if (isVideo) {
//                            Intent intent = new Intent(MainActivity.this, VideoEditActivity.class);
//                            intent.putExtra(VideoEditActivity.PATH, pathList.get(0));
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(MainActivity.this, ImageEditActivity.class);
//                            intent.putExtra(ImageEditActivity.PATH, pathList.get(0));
//                            startActivity(intent);
//                        }
//                    }
//                })
//                .scanMedia();
    }
}
