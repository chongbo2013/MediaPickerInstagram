package me.ningsk.filterlibrary.glfilter.color;

import android.content.Context;
import android.text.TextUtils;

import me.ningsk.filterlibrary.glfilter.base.GLImageGroupFilter;
import me.ningsk.filterlibrary.glfilter.color.bean.DynamicColor;

/**
 * <p>描述：颜色滤镜<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 11 19<br>
 * 版本：v1.0<br>
 */
public class GLImageDynamicColorFilter extends GLImageGroupFilter {

    public GLImageDynamicColorFilter(Context context, DynamicColor dynamicColor) {
        super(context);
        // 判断数据是否存在
        if (dynamicColor == null || dynamicColor.filterList == null
                || TextUtils.isEmpty(dynamicColor.unzipPath)) {
            return;
        }
        // 添加滤镜
        for (int i = 0; i < dynamicColor.filterList.size(); i++) {
            mFilters.add(new DynamicColorFilter(context, dynamicColor.filterList.get(i), dynamicColor.unzipPath));
        }
    }

    /**
     * 设置滤镜强度
     * @param strength
     */
    public void setStrength(float strength) {
        for (int i = 0; i < mFilters.size(); i++) {
            if (mFilters.get(i) != null && mFilters.get(i) instanceof DynamicColorBaseFilter) {
                ((DynamicColorBaseFilter) mFilters.get(i)).setStrength(strength);
            }
        }
    }
}
