package me.ningsk.videolibrary.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.filterlibrary.glfilter.resource.bean.ResourceData;

/**
 * <p>描述：滤镜特效处理基类<p>
 * 作者：ningsk<br>
 * 日期：2018/10/31 09 27<br>
 * 版本：v1.0<br>
 */
public abstract class BaseVideoFilterFragment extends BaseVideoPageFragment {

    protected List<ResourceData> mFilterDataList;
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
     * 获取滤镜数据
     * @param position
     * @return
     */
    public ResourceData getFilterData(int position) {
        if (mFilterDataList.size() <= position) {
            return null;
        }
        return mFilterDataList.get(position);
    }

}