package me.ningsk.videolibrary.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import me.ningsk.filterlibrary.glfilter.utils.GLImageFilterType;

/**
 * <p>描述：滤镜特效处理基类<p>
 * 作者：ningsk<br>
 * 日期：2018/10/31 09 27<br>
 * 版本：v1.0<br>
 */
public abstract class BaseVideoFilterFragment extends BaseVideoPageFragment {

    protected ArrayList<GLImageFilterType> mGlFilterType = new ArrayList<>();
    protected ArrayList<String> mFilterName = new ArrayList<>();

    protected RecyclerView mFilterListView;
    protected LinearLayoutManager mFilterLayoutManager;

    public BaseVideoFilterFragment() {
        initFilters();
    }

    /**
     * 初始化滤镜组
     */
    protected abstract void initFilters();

    /**
     * 获取滤镜类型
     * @param position
     * @return
     */
    public GLImageFilterType getFilterType(int position) {
        if (mGlFilterType.size() <= position) {
            return GLImageFilterType.NONE;
        }
        return mGlFilterType.get(position);
    }

}

