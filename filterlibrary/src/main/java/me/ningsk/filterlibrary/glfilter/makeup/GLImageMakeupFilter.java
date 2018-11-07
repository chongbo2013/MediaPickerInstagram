package me.ningsk.filterlibrary.glfilter.makeup;

import android.content.Context;

import me.ningsk.filterlibrary.glfilter.base.GLImageFilter;
import me.ningsk.filterlibrary.glfilter.makeup.bean.IMakeup;
import me.ningsk.filterlibrary.glfilter.makeup.bean.MakeupParam;

/**
 * <p>描述：彩妆滤镜<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 11 06<br>
 * 版本：v1.0<br>
 */
public class GLImageMakeupFilter extends GLImageFilter implements IMakeup {

    public GLImageMakeupFilter(Context context) {
        super(context);
    }

    public GLImageMakeupFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }

    @Override
    public void onMakeup(MakeupParam makeup) {

    }

}
