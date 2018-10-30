package me.ningsk.cameralibrary.engine;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import me.ningsk.cameralibrary.engine.model.AspectRatio;

/**
 * <p>描述：相机预览引擎<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 17 07<br>
 * 版本：v1.0<br>
 */
public final class PreviewEngine {

    private WeakReference<Activity> mWeakActivity;
    private WeakReference<Fragment> mWeakFragment;

    private PreviewEngine(Activity activity) {
        this(activity, null);
    }

    private PreviewEngine(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private PreviewEngine(Activity activity, Fragment fragment) {
        mWeakActivity = new WeakReference<>(activity);
        mWeakFragment = new WeakReference<>(fragment);
    }

    public static PreviewEngine from(Activity activity) {
        return new PreviewEngine(activity);
    }

    public static PreviewEngine from(Fragment fragment) {
        return new PreviewEngine(fragment);
    }

    /**
     * 设置长宽比
     * @param ratio
     * @return
     */
    public PreviewBuilder setCameraRatio(AspectRatio ratio) {
        return new PreviewBuilder(this, ratio);
    }

    public Activity getActivity() {
        return mWeakActivity.get();
    }

    public Fragment getFragment() {
        return mWeakFragment.get();
    }


}

