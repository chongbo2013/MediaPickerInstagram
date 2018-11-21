package com.jerei.mediapickerinstagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jerei.mediapickerinstagram.fragment.CapturePreviewFragment;

import me.ningsk.cameralibrary.listener.OnPageOperationListener;

public class CustomCameraActivity extends AppCompatActivity implements OnPageOperationListener {
    private static final String FRAGMENT_CAMERA = "fragment_camera";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);
        if (null == savedInstanceState) {
            CapturePreviewFragment fragment = new CapturePreviewFragment();
            fragment.setOnPageOperationListener(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, FRAGMENT_CAMERA)
                    .addToBackStack(FRAGMENT_CAMERA)
                    .commit();
        }
    }

    @Override
    public void onOpenGalleryPage() {

    }

    @Override
    public void onOpenImageEditPage(String path) {
        startActivity(new Intent(CustomCameraActivity.this, YiLaBaoActivity.class));
    }

    @Override
    public void onOpenVideoEditPage(String path) {

    }
}
