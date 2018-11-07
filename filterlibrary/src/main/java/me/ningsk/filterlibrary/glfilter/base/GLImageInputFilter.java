package me.ningsk.filterlibrary.glfilter.base;

import android.content.Context;

import me.ningsk.filterlibrary.glfilter.utils.OpenGLUtils;


/**
 * <p>描述：加载一张图片，需要倒过来<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 12<br>
 * 版本：v1.0<br>
 */
public class GLImageInputFilter extends GLImageFilter {

    public GLImageInputFilter(Context context) {
        this(context, VERTEX_SHADER, OpenGLUtils.getShaderFromAssets(context,
                "shader/base/fragment_image_input.glsl"));
    }

    public GLImageInputFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }
}
