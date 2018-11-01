package me.ningsk.mediascanlibrary.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.config.SelectionOptions;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.entity.LocalMediaFolder;
import me.ningsk.mediascanlibrary.utils.LogUtils;
import me.ningsk.mediascanlibrary.utils.MediaComparator;


public class MediaLoader implements LoaderManager.LoaderCallbacks<ArrayList<LocalMedia>> {
    private static final int LOADER_ID = 1;

    private MediaCallBack mMediaCallBack;
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;

    public void onCreate(FragmentActivity activity, MediaCallBack callBack) {
        mContext = new WeakReference<>(activity.getApplicationContext());
        mLoaderManager = activity.getSupportLoaderManager();
        mMediaCallBack = callBack;
    }

    public void loadMedia() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<LocalMedia>> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (null == context) {
            return null;
        }
        return new AsyncMediaLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<LocalMedia>> loader, ArrayList<LocalMedia> localMediaList) {
        if (null == mContext.get()) {
            return;
        }
        if (null == mMediaCallBack) {
            return;
        }
        if (localMediaList == null || localMediaList.isEmpty()) {
            return;
        }

        long start = System.currentTimeMillis();
        ArrayList<LocalMedia> fileList;
        Map<String, ArrayList<LocalMedia>> map = new TreeMap<>();

        ArrayList<LocalMedia> selectedItems = SelectionOptions.getOptions().selectedItems;
        for (LocalMedia localMedia : localMediaList) {
            if (selectedItems != null && !selectedItems.isEmpty()) {
                for (LocalMedia selectedItem : selectedItems) {
                    if (selectedItem.equals(localMedia)) {
                        localMedia.setSelected(true);
                        break;
                    }
                }
            }
            String folderName = new File(localMedia.getPath()).getParentFile().getName();
            if (!map.containsKey(folderName)) {
                fileList = new ArrayList<>();
                fileList.add(localMedia);
                map.put(folderName, fileList);
            } else {
                fileList = map.get(folderName);
                fileList.add(localMedia);
            }
        }

        LocalMediaFolder folderBean = new LocalMediaFolder();
        folderBean.setName("全部");
        folderBean.setImageNum(localMediaList.size());
        folderBean.setMedias(localMediaList);

        ArrayList<LocalMediaFolder> folderList = new ArrayList<>();
        folderList.add(folderBean);
        for (Map.Entry<String, ArrayList<LocalMedia>> entry : map.entrySet()) {
            folderBean = new LocalMediaFolder();
            folderBean.setName(entry.getKey());
            folderBean.setMedias(entry.getValue());
            folderBean.setPath(entry.getValue().get(0).getPath());
            folderBean.setImageNum(entry.getValue().size());
            folderList.add(folderBean);

        }
        LogUtils.e("folder list size --> " + folderList.size());
        LogUtils.e("加载文件夹耗时 --> " + (System.currentTimeMillis() - start));
        mMediaCallBack.onLoadFinished(localMediaList, folderList);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LocalMedia>> loader) {
        if (null == mContext.get()) {
            return;
        }
        if (null == mMediaCallBack) {
            return;
        }
        mMediaCallBack.onLoaderReset();
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        if (mContext != null)
            mContext.clear();
        mMediaCallBack = null;
    }

    private static class AsyncMediaLoader extends WrappedAsyncTaskLoader<ArrayList<LocalMedia>> {
        AsyncMediaLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<LocalMedia> loadInBackground() {
            long start = System.currentTimeMillis();
            ArrayList<LocalMedia> localMediaList = new ArrayList<>();
            boolean showGif = SelectionOptions.getOptions().showGif;
            switch (SelectionOptions.getOptions().mimeType) {
                case MimeType.PHOTO:
                    localMediaList.addAll(PhotoLoader.getAllPhotos(getContext(), showGif));
                    break;
                case MimeType.VIDEO:
                    localMediaList.addAll(VideoLoader.getAllVideos(getContext()));
                    break;
                case MimeType.ALL:
                    localMediaList.addAll(PhotoLoader.getAllPhotos(getContext(), showGif));
                    localMediaList.addAll(VideoLoader.getAllVideos(getContext()));
                    break;
            }
            Collections.sort(localMediaList, new MediaComparator());
            LogUtils.e("loadInBackground 耗时 --> " + (System.currentTimeMillis() - start));
            return localMediaList;
        }
    }

    public interface MediaCallBack {
        void onLoadFinished(ArrayList<LocalMedia> localMediaList, ArrayList<LocalMediaFolder> folderList);

        void onLoaderReset();
    }
}
