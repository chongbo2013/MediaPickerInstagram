package me.ningsk.imagelibrary.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

import me.ningsk.filterlibrary.glfilter.resource.FilterHelper;
import me.ningsk.filterlibrary.glfilter.resource.bean.ResourceData;
import me.ningsk.filterlibrary.widget.GLImageSurfaceView;
import me.ningsk.imagelibrary.R;
import me.ningsk.imagelibrary.adapter.ImageFilterAdapter;


/**
 * <p>描述：滤镜编辑界面<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 18 10<br>
 * 版本：v1.0<br>
 */
public class ImageFilterFragment extends Fragment {

    private View mContentView;
    private RelativeLayout mLayoutFilterContent;
    private LinearLayout mLayoutContent;
    private GLImageSurfaceView mCainImageView;
    private RecyclerView mFiltersView;
    private LinearLayoutManager mLayoutManager;
    private int screenWidth;


    private Activity mActivity;

    private Bitmap mBitmap;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_image_filter, container, false);
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(mContentView);
    }

    /**
     * 初始化视图
     *
     * @param view
     */
    private void initView(View view) {
        // 图片内容布局
        mCainImageView = view.findViewById(R.id.glImageView);
        mLayoutContent = view.findViewById(R.id.layout_content);
        resizeLiner();
        if (mBitmap != null) {
            mCainImageView.setBitmap(mBitmap);
        }
        // 滤镜内容框
        mLayoutFilterContent = view.findViewById(R.id.layout_filter_content);
        showFilters();
    }

    private void resizeLiner() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLayoutContent.getLayoutParams();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        layoutParams.width = screenWidth;
        layoutParams.height = screenWidth;
        mLayoutContent.post(() -> {
            mLayoutContent.setLayoutParams(layoutParams);
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mCainImageView != null) {
            mCainImageView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCainImageView != null) {
            mCainImageView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        mContentView = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    /**
     * 显示滤镜列表
     */
    private void showFilters() {

        mFiltersView = mContentView.findViewById(R.id.filter_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFiltersView.setLayoutManager(mLayoutManager);
        ImageFilterAdapter adapter = new ImageFilterAdapter(mActivity, FilterHelper.getFilterList());
        mFiltersView.setAdapter(adapter);
        adapter.addOnFilterChangeListener(resourceData -> {
            if (mCainImageView != null) {
                mCainImageView.setFilter(resourceData);
            }
        });

    }

    /**
     * 设置bitmap
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        if (mCainImageView != null) {
            mCainImageView.setBitmap(mBitmap);
        }
    }

    /**
     * 是否显示GLSurfaceView，解决多重fragment时显示问题
     *
     * @param showing
     */
    public void showGLSurfaceView(boolean showing) {
        if (mCainImageView != null) {
            mCainImageView.setVisibility(showing ? View.VISIBLE : View.GONE);
        }
    }
}
