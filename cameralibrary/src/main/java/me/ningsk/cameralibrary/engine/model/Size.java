package me.ningsk.cameralibrary.engine.model;

/**
 * <p>描述：尺寸相关<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 11<br>
 * 版本：v1.0<br>
 */
public class Size {
    int mWidth;
    int mHeight;

    public Size() {
    }

    public Size(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

}
