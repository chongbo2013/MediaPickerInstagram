package me.ningsk.cameralibrary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.ningsk.cameralibrary.R;

/**
 * <p>描述：音视频选择<p>
 * 作者：ningsk<br>
 * 日期：2018/11/12 14 18<br>
 * 版本：v1.0<br>
 */
public class GalleryPickerFragment extends Fragment {
    // Fragment主页面
    private View mContentView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_gallery_picker, container, false);
        return mContentView;
    }
}
