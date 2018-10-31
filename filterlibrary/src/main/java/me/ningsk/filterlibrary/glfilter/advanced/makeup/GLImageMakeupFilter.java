package me.ningsk.filterlibrary.glfilter.advanced.makeup;

import android.content.Context;

import me.ningsk.filterlibrary.glfilter.base.GLImageFilter;
import me.ningsk.filterlibrary.glfilter.model.IMakeup;
import me.ningsk.filterlibrary.glfilter.model.Makeup;


/**
 * 彩妆滤镜
 */
public class GLImageMakeupFilter extends GLImageFilter implements IMakeup {

    public GLImageMakeupFilter(Context context) {
        super(context);
    }

    public GLImageMakeupFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }

    @Override
    public void onMakeup(Makeup makeup) {

    }

}
