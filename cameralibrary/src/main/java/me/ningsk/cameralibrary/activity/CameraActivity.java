package me.ningsk.cameralibrary.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import me.ningsk.cameralibrary.R;
import me.ningsk.cameralibrary.adapter.ViewPagerAdapter;
import me.ningsk.cameralibrary.engine.camera.CameraParam;
import me.ningsk.cameralibrary.engine.model.GalleryType;
import me.ningsk.cameralibrary.fragment.CapturePreviewFragment;
import me.ningsk.cameralibrary.fragment.GalleryPickerFragment;
import me.ningsk.cameralibrary.listener.OnPageOperationListener;
import me.ningsk.cameralibrary.widget.ToolbarView;
import me.ningsk.facedetectlibrary.FaceTracker;
import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.entity.LocalMedia;

/**
 * <p>描述：相机预览界面<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 17 11<br>
 * 版本：v1.0<br>
 */
public class CameraActivity extends AppCompatActivity implements OnPageOperationListener{

    private ViewPager mMainViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.photo_ins_style);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (null == savedInstanceState) {
            mMainViewPager = findViewById(R.id.mMainViewPager);
            final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
            mMainViewPager.setAdapter(pagerAdapter);
            mMainViewPager.setCurrentItem(0);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onOpenGalleryPage() {

    }

    @Override
    public void onOpenImageEditPage(String path) {
//        Intent intent = new Intent(CameraActivity.this, ImageEditActivity.class);
//        intent.putExtra(ImageEditActivity.PATH, path);
//        startActivityForResult(intent, 0);
    }

    @Override
    public void onOpenVideoEditPage(LocalMedia media) {
//
//        Intent intent = new Intent(CameraActivity.this, TrimVideoActivity.class);
//
//        intent.putExtra("path", media);
//        startActivityForResult(intent, 0);
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

            }
        };
    }





    private ArrayList<Fragment> getListFragment() {
        String[] galleryIndicator = getResources().getStringArray(R.array.gallery_indicator);
        ArrayList<Fragment> fragments = new ArrayList<>();
        GalleryPickerFragment galleryPickerFragment = new GalleryPickerFragment();
        galleryPickerFragment.setOnPageOperationListener(this);
        fragments.add(galleryPickerFragment);
        CapturePreviewFragment photoFragment = new CapturePreviewFragment();
        photoFragment.setOnPageOperationListener(this);
        fragments.add(photoFragment);

        return fragments;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10010) {
            ArrayList<LocalMedia> medias = new ArrayList<>();
            String type = data.getStringExtra("Type");
            if (type.equals("photo")) {
                String path = data.getStringExtra("Path");

                LocalMedia media = new LocalMedia();
                media.setPath(path);
                Glide.with(getApplicationContext()).asBitmap().load(path).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        media.setWidth(width);
                        media.setHeight(height);
                        media.setMediaMimeType(MimeType.PHOTO);
                    }
                });
                medias.add(media);
            } else {


                LocalMedia media = new LocalMedia();
                media = data.getParcelableExtra("media");
                media.setMediaMimeType(MimeType.VIDEO);
                medias.add(media);
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("selectImage",medias);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    /**
     * Close Activity
     */
    public void closeActivity() {
        finish();
        overridePendingTransition(0, R.anim.a3);
    }

}