package me.ningsk.cameralibrary.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

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

/**
 * <p>描述：相机预览界面<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 17 11<br>
 * 版本：v1.0<br>
 */
public class CameraActivity extends AppCompatActivity implements OnPageOperationListener, ToolbarView.OnClickBackListener,
        ToolbarView.OnClickTitleListener, ToolbarView.OnClickNextListener{

    private TabLayout mMainTabLayout;
    private ViewPager mMainViewPager;
    private ToolbarView mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            mMainTabLayout = findViewById(R.id.mMainTabLayout);
            mMainViewPager = findViewById(R.id.mMainViewPager);
            mToolbar = findViewById(R.id.mToolbar);
            mToolbar.setOnClickBackListener(this);
            mToolbar.setOnClickTitleListener(this);
            mToolbar.setOnClickNextListener(this);
            mMainTabLayout.setSelectedTabIndicatorHeight(0);
            final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
            mMainViewPager.setAdapter(pagerAdapter);
            mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
            mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));
            mMainViewPager.setCurrentItem(0);
        }
        faceTrackerRequestNetwork();
    }

    /**
     * 人脸检测SDK验证，可以替换成自己的SDK
     */
    private void faceTrackerRequestNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceTracker.requestFaceNetwork(CameraActivity.this);
            }
        }).start();
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
        if (CameraParam.getInstance().gallerySelectedListener != null) {
            CameraParam.getInstance().gallerySelectedListener.onGalleryClickListener(GalleryType.WITHOUT_GIF);
        }
    }

    @Override
    public void onOpenImageEditPage(String path) {
        if (CameraParam.getInstance().captureListener != null) {
            CameraParam.getInstance().captureListener.onMediaSelectedListener(path, GalleryType.PICTURE);
        }
    }

    @Override
    public void onOpenVideoEditPage(String path) {
        if (CameraParam.getInstance().captureListener != null) {
            CameraParam.getInstance().captureListener.onMediaSelectedListener(path, GalleryType.VIDEO);
        }
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                displayTitleByTab(tab);
                initNextButtonByTab(tab.getPosition());
            }
        };
    }

    private void displayTitleByTab(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String title = tab.getText().toString();
            mToolbar.setTitle(title);
        }
    }

    private void initNextButtonByTab(int position) {
        switch (position) {
            case 0:
                mToolbar.showNext();
                break;
            case 1:
                mToolbar.hideNext();
                break;
            case 2:
                mToolbar.hideNext();
                break;
            default:
                mToolbar.hideNext();
                break;
        }
    }


    private ArrayList<Fragment> getListFragment() {
        String[] galleryIndicator = getResources().getStringArray(R.array.gallery_indicator);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new GalleryPickerFragment());
        mMainTabLayout.addTab(mMainTabLayout.newTab().setText(galleryIndicator[0]));
        CapturePreviewFragment photoFragment = new CapturePreviewFragment();
        photoFragment.setOnPageOperationListener(this);
        fragments.add(photoFragment);

        mMainTabLayout.addTab(mMainTabLayout.newTab().setText(galleryIndicator[1]));
        return fragments;
    }


    @Override
    public void onClickBack() {

    }

    @Override
    public void onClickNext() {

    }

    @Override
    public void onClickTitle() {

    }
}
