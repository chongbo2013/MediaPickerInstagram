package me.ningsk.photoselector.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import me.ningsk.photoselector.MimeType;
import me.ningsk.photoselector.SelectionOptions;
import me.ningsk.photoselector.bean.MediaBean;
import me.ningsk.photoselector.bean.FolderBean;
import me.ningsk.photoselector.utils.LogUtils;
import me.ningsk.photoselector.utils.MediaComparator;

public class MediaLoader implements LoaderManager.LoaderCallbacks<ArrayList<MediaBean>> {
    private static final int LOADER_ID = 1;

    private MediaCallBack mMediaCallBack;
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;

    public void onCreate(AppCompatActivity activity, MediaCallBack callBack) {
        mContext = new WeakReference<>(activity.getApplicationContext());
        mLoaderManager = activity.getSupportLoaderManager();
        mMediaCallBack = callBack;
    }

    public void loadMedia() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    public void restartLoadMedia() {
        mLoaderManager.restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<MediaBean>> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (null == context) {
            return null;
        }
        return new AsyncMediaLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MediaBean>> loader, ArrayList<MediaBean> mediaBeanList) {
        if (null == mContext.get()) {
            return;
        }
        if (null == mMediaCallBack) {
            return;
        }
        if (mediaBeanList == null || mediaBeanList.isEmpty()) {
            return;
        }

        long start = System.currentTimeMillis();
        ArrayList<MediaBean> fileList;
        Map<String, ArrayList<MediaBean>> map = new TreeMap<>();

        ArrayList<MediaBean> selectedItems = SelectionOptions.getOptions().selectedItems;
        for (MediaBean mediaBean : mediaBeanList) {
            if (selectedItems != null && !selectedItems.isEmpty()) {
                for (MediaBean selectedItem : selectedItems) {
                    if (selectedItem.equals(mediaBean)) {
                        mediaBean.setSelected(true);
                        break;
                    }
                }
            }
            String folderName = new File(mediaBean.getPath()).getParentFile().getName();
            if (!map.containsKey(folderName)) {
                fileList = new ArrayList<>();
                fileList.add(mediaBean);
                map.put(folderName, fileList);
            } else {
                fileList = map.get(folderName);
                fileList.add(mediaBean);
            }
        }

        FolderBean folderBean = new FolderBean();
        folderBean.setFolderName("全部");
        folderBean.setFileList(mediaBeanList);

        ArrayList<FolderBean> folderList = new ArrayList<>();
        folderList.add(folderBean);
        for (Map.Entry<String, ArrayList<MediaBean>> entry : map.entrySet()) {
            folderBean = new FolderBean();
            folderBean.setFolderName(entry.getKey());
            folderBean.setFileList(entry.getValue());
            folderList.add(folderBean);
        }
        LogUtils.e("folder list size --> " + folderList.size());
        LogUtils.e("加载文件夹耗时 --> " + (System.currentTimeMillis() - start));
        mMediaCallBack.onLoadFinished(mediaBeanList, folderList);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MediaBean>> loader) {
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

    private static class AsyncMediaLoader extends WrappedAsyncTaskLoader<ArrayList<MediaBean>> {
        AsyncMediaLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<MediaBean> loadInBackground() {
            long start = System.currentTimeMillis();
            ArrayList<MediaBean> mediaBeanList = new ArrayList<>();
            boolean showGif = SelectionOptions.getOptions().showGif;
            switch (SelectionOptions.getOptions().mimeType) {
                case MimeType.PHOTO:
                    mediaBeanList.addAll(PhotoLoader.getAllPhotos(getContext(), showGif));
                    break;
                case MimeType.VIDEO:
                    mediaBeanList.addAll(VideoLoader.getAllVideos(getContext()));
                    break;
                case MimeType.ALL:
                    mediaBeanList.addAll(PhotoLoader.getAllPhotos(getContext(), showGif));
                    mediaBeanList.addAll(VideoLoader.getAllVideos(getContext()));
                    break;
            }
            Collections.sort(mediaBeanList, new MediaComparator());
            LogUtils.e("loadInBackground 耗时 --> " + (System.currentTimeMillis() - start));
            return mediaBeanList;
        }
    }

    public interface MediaCallBack {
        void onLoadFinished(ArrayList<MediaBean> mediaBeanList, ArrayList<FolderBean> folderList);

        void onLoaderReset();
    }
}
