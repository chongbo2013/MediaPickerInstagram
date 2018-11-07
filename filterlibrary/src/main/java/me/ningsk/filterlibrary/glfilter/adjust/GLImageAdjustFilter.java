package me.ningsk.filterlibrary.glfilter.adjust;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.filterlibrary.glfilter.adjust.bean.AdjustParam;
import me.ningsk.filterlibrary.glfilter.adjust.bean.IAdjust;
import me.ningsk.filterlibrary.glfilter.base.GLImageFilter;
import me.ningsk.filterlibrary.glfilter.base.GLImageGroupFilter;

/**
 * <p>描述：调节滤镜<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 22<br>
 * 版本：v1.0<br>
 */
public class GLImageAdjustFilter extends GLImageGroupFilter implements IAdjust {

    // 滤镜索引
    private static final int IndexBrightness = 0;
    private static final int IndexContrast = 1;
    private static final int IndexExposure = 2;
    private static final int IndexHue = 3;
    private static final int IndexSaturation = 4;
    private static final int IndexSharpen = 5;

    public GLImageAdjustFilter(Context context) {
        this(context, initFilters(context));
    }

    public GLImageAdjustFilter(Context context, List<GLImageFilter> filters) {
        super(context, filters);
    }

    private static List<GLImageFilter> initFilters(Context context) {
        List<GLImageFilter> filters = new ArrayList<>();
        filters.add(IndexBrightness, new GLImageBrightnessFilter(context));
        filters.add(IndexContrast, new GLImageContrastFilter(context));
        filters.add(IndexExposure, new GLImageExposureFilter(context));
        filters.add(IndexHue, new GLImageHueFilter(context));
        filters.add(IndexSaturation, new GLImageSaturationFilter(context));
        filters.add(IndexSharpen, new GLImageSharpenFilter(context));
        return filters;
    }

    @Override
    public void onAdjust(AdjustParam adjust) {
        if (adjust == null) {
            return;
        }
        if (mFilters.get(IndexBrightness) != null) {
            ((GLImageBrightnessFilter) mFilters.get(IndexBrightness)).setBrightness(adjust.brightness);
        }
        if (mFilters.get(IndexContrast) != null) {
            ((GLImageContrastFilter) mFilters.get(IndexContrast)).setContrast(adjust.contrast);
        }
        if (mFilters.get(IndexExposure) != null) {
            ((GLImageExposureFilter) mFilters.get(IndexExposure)).setExposure(adjust.exposure);
        }
        if (mFilters.get(IndexHue) != null) {
            ((GLImageHueFilter) mFilters.get(IndexHue)).setHue(adjust.hue);
        }
        if (mFilters.get(IndexSaturation) != null) {
            ((GLImageSaturationFilter) mFilters.get(IndexSaturation)).setSaturation(adjust.saturation);
        }
        if (mFilters.get(IndexSharpen) != null) {
            ((GLImageSharpenFilter) mFilters.get(IndexSharpen)).setSharpness(adjust.sharpness);
        }
    }
}
