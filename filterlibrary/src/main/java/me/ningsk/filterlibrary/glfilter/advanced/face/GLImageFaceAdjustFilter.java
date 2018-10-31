package me.ningsk.filterlibrary.glfilter.advanced.face;

import android.content.Context;

import me.ningsk.filterlibrary.glfilter.base.GLImageDrawElementsFilter;
import me.ningsk.filterlibrary.glfilter.model.Beauty;
import me.ningsk.filterlibrary.glfilter.model.IBeautify;


/**
 * 美型滤镜
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
    public void onBeauty(Beauty beauty) {

    }
}
