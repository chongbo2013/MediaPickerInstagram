package me.ningsk.filterlibrary.glfilter.adjust;

import android.content.Context;
import android.opengl.GLES30;

import me.ningsk.filterlibrary.glfilter.base.GLImageFilter;
import me.ningsk.filterlibrary.glfilter.utils.OpenGLUtils;


/**
 * 锐度变换
 */

public class GLImageSharpenFilter extends GLImageFilter {

    private int mSharpnessLoc;
    private float mSharpness;
    private int mImageWidthFactorHandle;
    private int mImageHeightFactorHandle;

    public GLImageSharpenFilter(Context context) {
        this(context, OpenGLUtils.getShaderFromAssets(context, "shader/adjust/vertex_sharpen.glsl"),
                OpenGLUtils.getShaderFromAssets(context, "shader/adjust/fragment_sharpen.glsl"));
    }

    public GLImageSharpenFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        mImageWidthFactorHandle = GLES30.glGetUniformLocation(mProgramHandle, "imageWidthFactor");
        mImageHeightFactorHandle = GLES30.glGetUniformLocation(mProgramHandle, "imageHeightFactor");
        mSharpnessLoc = GLES30.glGetUniformLocation(mProgramHandle, "sharpness");
        setSharpness(0);
    }

    @Override
    public void onInputSizeChanged(int width, int height) {
        super.onInputSizeChanged(width, height);
        setFloat(mImageWidthFactorHandle, 1.0f / width);
        setFloat(mImageHeightFactorHandle, 1.0f / height);
    }

    /**
     * 设置锐度
     * @param sharpness -4.0 ~ 4.0, 默认为0
     */
    public void setSharpness(float sharpness) {
        if (sharpness < -4.0) {
            sharpness = -4.0f;
        } else if (sharpness > 4.0f) {
            sharpness = 4.0f;
        }
        mSharpness = sharpness;
        setFloat(mSharpnessLoc, mSharpness);
    }
}
