package me.ningsk.photoselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import me.ningsk.photoselector.bean.FolderBean;
import me.ningsk.photoselector.bean.MediaBean;
import me.ningsk.photoselector.loader.MediaLoader;

public class PhotoSelectorActivity extends AppCompatActivity implements MediaLoader.MediaCallBack, AdapterView.OnItemClickListener {

    private SelectionOptions mOptions;
    private final MediaLoader mMediaLoader = new MediaLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onLoadFinished(ArrayList<MediaBean> mediaBeanList, ArrayList<FolderBean> folderList) {

    }

    @Override
    public void onLoaderReset() {

    }
}
