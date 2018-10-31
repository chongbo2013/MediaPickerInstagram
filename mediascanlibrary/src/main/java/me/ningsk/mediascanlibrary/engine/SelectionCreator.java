package me.ningsk.mediascanlibrary.engine;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.activity.PhotoSelectorActivity;
import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.config.SelectionOptions;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.utils.DoubleUtils;

public final class SelectionCreator {
    private final MediaScanEngine mMediaScanEngine;
    private final SelectionOptions mOptions;

    SelectionCreator(MediaScanEngine mediaScanEngine, @MimeType.MediaMimeType int mimeType) {
        mMediaScanEngine = mediaScanEngine;
        mOptions = SelectionOptions.getCleanOptions();
        mOptions.mimeType = mimeType;
    }



    /**
     * @param themeStyleId MediaScanEngine Theme style
     * @return
     */
    public SelectionCreator theme(@StyleRes int themeStyleId) {
        mOptions.themeId = themeStyleId;
        return this;
    }

    /**
     * @param selectionMode MediaScanEngine Selection model and PictureConfig.MULTIPLE or PictureConfig.SINGLE
     * @return
     */
    public SelectionCreator selectionMode(int selectionMode) {
        mOptions.mimeType = selectionMode;
        return this;
    }


    /**
     * @param maxSelectNum MediaScanEngine max selection
     * @return
     */
    public SelectionCreator maxSelectNum(int maxSelectNum) {
        mOptions.maxSelectNum = maxSelectNum;
        return this;
    }

    /**
     * @param minSelectNum MediaScanEngine min selection
     * @return
     */
    public SelectionCreator minSelectNum(int minSelectNum) {
        mOptions.minSelectNum = minSelectNum;
        return this;
    }





    /**
     * @param videoMaxSecond selection video max second
     * @return
     */
    public SelectionCreator videoMaxSecond(int videoMaxSecond) {
        mOptions.videoMaxSecond = videoMaxSecond * 1000;
        return this;
    }

    /**
     * @param videoMinSecond selection video min second
     * @return
     */
    public SelectionCreator videoMinSecond(int videoMinSecond) {
        mOptions.videoMinSecond = videoMinSecond * 1000;
        return this;
    }




    /**
     * @param width  glide width
     * @param height glide height
     * @return
     */
    public SelectionCreator glideOverride(@IntRange(from = 100) int width,
                                               @IntRange(from = 100) int height) {
        mOptions.overrideWidth = width;
        mOptions.overrideHeight = height;
        return this;
    }

    /**
     * @param sizeMultiplier The multiplier to apply to the
     *                       {@link com.bumptech.glide.request.target.Target}'s dimensions when
     *                       loading the resource.
     * @return
     */
    public SelectionCreator sizeMultiplier(@FloatRange(from = 0.1f) float sizeMultiplier) {
        mOptions.sizeMultiplier = sizeMultiplier;
        return this;
    }

    /**
     * @param gridSize MediaScanEngine image span count
     * @return
     */
    public SelectionCreator imageSpanCount(int gridSize) {
        mOptions.gridSize = gridSize;
        return this;
    }

    /**
     * @param zoomAnim Picture list zoom anim
     * @return
     */
    public SelectionCreator isZoomAnim(boolean zoomAnim) {
        mOptions.zoomAnim = zoomAnim;
        return this;
    }


    /**
     * @param showGif Whether to open gif
     * @return
     */
    public SelectionCreator showGif(boolean showGif) {
        mOptions.showGif = showGif;
        return this;
    }


    /**
     * @param selectionMedia Select the selected picture set
     * @return
     */
    public SelectionCreator selectionMedia(ArrayList<LocalMedia> selectionMedia) {
        if (selectionMedia == null) {
            selectionMedia = new ArrayList<>();
        }
        mOptions.selectedItems = selectionMedia;
        return this;
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    public void forResult(int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = mMediaScanEngine.getActivity();
            if (activity == null) {
                return;
            }
            Intent intent = new Intent(activity, PhotoSelectorActivity.class);
            Fragment fragment = mMediaScanEngine.getFragment();
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
            activity.overridePendingTransition(R.anim.a5, 0);
        }
    }

}


