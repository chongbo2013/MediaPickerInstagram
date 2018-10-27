package me.ningsk.videorecord.filters;

import android.content.res.Resources;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import me.ningsk.videorecord.filters.BaseFilter;

public class OESFilter extends BaseFilter {

    public OESFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/oes_base_vertex.sh","shader/oes_base_fragment.sh");
    }
    @Override
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType());
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,getTextureId());
        GLES20.glUniform1i(mHTexture,getTextureType());
    }
    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
