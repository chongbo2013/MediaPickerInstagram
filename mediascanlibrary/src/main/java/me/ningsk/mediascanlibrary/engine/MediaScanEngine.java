package me.ningsk.mediascanlibrary.engine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.ningsk.mediascanlibrary.config.PhotoSelectorConfig;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
public final class MediaScanEngine {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;

    private MediaScanEngine(Activity activity) {
        this(activity, null);
    }

    private MediaScanEngine(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private MediaScanEngine(Activity activity, Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    /**
     * Start MediaScanEngine for Activity.
     *
     * @param activity
     * @return MediaScanEngine instance.
     */
    public static MediaScanEngine from(Activity activity) {
        return new MediaScanEngine(activity);
    }

    /**
     * Start MediaScanEngine for Fragment.
     *
     * @param fragment
     * @return MediaScanEngine instance.
     */
    public static MediaScanEngine from(Fragment fragment) {
        return new MediaScanEngine(fragment);
    }

    /**
     * @param mimeType Select the type of picture you want，all or Picture or Video .
     * @return LocalMedia PictureSelectionModel
     */
    public SelectionCreator openGallery(int mimeType) {
        return new SelectionCreator(this, mimeType);
    }


    /**
     * @param data
     * @return Selector Multiple LocalMedia
     */
    public static List<LocalMedia> obtainMultipleResult(Intent data) {
        List<LocalMedia> result = new ArrayList<>();
        if (data != null) {
            result = (List<LocalMedia>) data.getSerializableExtra(PhotoSelectorConfig.EXTRA_RESULT_SELECTION);
            if (result == null) {
                result = new ArrayList<>();
            }
            return result;
        }
        return result;
    }

    /**
     * @param data
     * @return Put image Intent Data
     */
    public static Intent putIntentResult(List<LocalMedia> data) {
        return new Intent().putExtra(PhotoSelectorConfig.EXTRA_RESULT_SELECTION, (Serializable) data);
    }

    /**
     * @param bundle
     * @return get Selector  LocalMedia
     */
    public static List<LocalMedia> obtainSelectorList(Bundle bundle) {
        List<LocalMedia> selectionMedias;
        if (bundle != null) {
            selectionMedias = (List<LocalMedia>) bundle
                    .getSerializable(PhotoSelectorConfig.EXTRA_SELECT_LIST);
            return selectionMedias;
        }
        selectionMedias = new ArrayList<>();
        return selectionMedias;
    }

    /**
     * @param selectedImages
     * @return put Selector  LocalMedia
     */
    public static void saveSelectorList(Bundle outState, List<LocalMedia> selectedImages) {
        outState.putSerializable(PhotoSelectorConfig.EXTRA_SELECT_LIST, (Serializable) selectedImages);
    }



    /**
     * @return Activity.
     */
    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }

    /**
     * @return Fragment.
     */
    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}


