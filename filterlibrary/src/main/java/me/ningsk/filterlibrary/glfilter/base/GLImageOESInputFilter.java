package me.ningsk.filterlibrary.glfilter.base;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;

import me.ningsk.filterlibrary.glfilter.utils.OpenGLUtils;


/**
 * <p>描述：外部纹理（OES纹理）输入<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 12<br>
 * 版本：v1.0<br>
 */

public class GLImageOESInputFilter extends GLImageFilter {

    private int mTransformMatrixHandle;
    private float[] mTransformMatrix;

    public GLImageOESInputFilter(Context context) {
        this(context, OpenGLUtils.getShaderFromAssets(context, "shader/base/vertex_oes_input.glsl"),
                OpenGLUtils.getShaderFromAssets(context, "shader/base/fragment_oes_input.glsl"));
    }

    public GLImageOESInputFilter(Context context, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
    }

    @Override
    public void initProgramHandle() {
        super.initProgramHandle();
        mTransformMatrixHandle = GLES30.glGetUniformLocation(mProgramHandle, "transformMatrix");
    }

    @Override
    public int getTextureType() {
        return GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
    }

    @Override
    public void onDrawFrameBegin() {
        super.onDrawFrameBegin();
        GLES30.glUniformMatrix4fv(mTransformMatrixHandle, 1, false, mTransformMatrix, 0);
    }

    /**
     * 设置SurfaceTexture的变换矩阵
     * @param transformMatrix
     */
    public void setTextureTransformMatrix(float[] transformMatrix) {
        mTransformMatrix = transformMatrix;
    }

}
