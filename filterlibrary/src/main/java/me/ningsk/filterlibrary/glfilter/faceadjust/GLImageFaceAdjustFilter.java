package me.ningsk.filterlibrary.glfilter.faceadjust;

import android.content.Context;

import me.ningsk.filterlibrary.glfilter.base.GLImageDrawElementsFilter;
import me.ningsk.filterlibrary.glfilter.beauty.bean.BeautyParam;
import me.ningsk.filterlibrary.glfilter.beauty.bean.IBeautify;

/**
 * <p>描述：美型滤镜<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 11 06<br>
 * 版本：v1.0<br>
 */
public class GLImageFaceAdjustFilter extends GLImageDrawElementsFilter implements IBeautify {

    public GLImageFaceAdjustFilter(Context context) {
        super(context);
    }

    public GLImageFaceAdjustFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }

    /**
     * 美型参数
     * @param beauty
     */
    @Override
    public void onBeauty(BeautyParam beauty) {

    }
}

