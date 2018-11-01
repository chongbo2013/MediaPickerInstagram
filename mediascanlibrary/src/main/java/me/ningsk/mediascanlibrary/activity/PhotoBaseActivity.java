package me.ningsk.mediascanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.config.PhotoSelectorConfig;
import me.ningsk.mediascanlibrary.config.SelectionOptions;
import me.ningsk.mediascanlibrary.dialog.PhotoDialog;
import me.ningsk.mediascanlibrary.engine.MediaScanEngine;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.immersive.ImmersiveManage;
import me.ningsk.mediascanlibrary.utils.AttrsUtils;
import me.ningsk.mediascanlibrary.utils.DoubleUtils;


public class PhotoBaseActivity extends FragmentActivity {
    protected Context mContext;
    protected SelectionOptions mOptions;
    protected int colorPrimary, colorPrimaryDark;

    protected List<LocalMedia> selectionMedias;

    /**
     * 是否使用沉浸式，子类复写该方法来确定是否采用沉浸式
     *
     * @return 是否沉浸式，默认true
     */
    @Override
    public boolean isImmersive() {
        return true;
    }

    /**
     * 具体沉浸的样式，可以根据需要自行修改状态栏和导航栏的颜色
     */
    public void immersive() {
        ImmersiveManage.immersiveAboveAPI23(this
                , colorPrimaryDark
                , colorPrimary
                , false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mOptions = savedInstanceState.getParcelable(PhotoSelectorConfig.EXTRA_CONFIG);
        } else {
            mOptions = SelectionOptions.getOptions();
        }
        int themeStyleId = mOptions.themeId;
        setTheme(themeStyleId);
        super.onCreate(savedInstanceState);
        mContext = this;
        initConfig();
        if (isImmersive()) {
            immersive();
        }

    }

    /**
     * 获取配置参数
     */
    private void initConfig() {
        // 标题栏背景色
        colorPrimary = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimary);
        // 状态栏背景色
        colorPrimaryDark = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimaryDark);
        // 已选图片列表
        selectionMedias = mOptions.selectedItems;
        if (selectionMedias == null) {
            selectionMedias = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoSelectorConfig.EXTRA_CONFIG, mOptions);
    }

    protected void startActivity(Class clz, Bundle bundle) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(this, clz);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    protected void startActivity(Class clz, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(this, clz);
            intent.putExtras(bundle);
            startActivityForResult(intent, requestCode);
        }
    }




    protected void handlerResult(List<LocalMedia> result) {
        onResult(result);
    }

    /**
     * return image result
     *
     * @param images
     */
    protected void onResult(List<LocalMedia> images) {
        if (mOptions.selectionMode == PhotoSelectorConfig.MULTIPLE
                && selectionMedias != null) {
            images.addAll(images.size() > 0 ? images.size() - 1 : 0, selectionMedias);
        }
        Intent intent = MediaScanEngine.putIntentResult(images);
        setResult(RESULT_OK, intent);
        closeActivity();
    }

    /**
     * Close Activity
     */
    public void closeActivity() {
        finish();
        overridePendingTransition(0, R.anim.a3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
