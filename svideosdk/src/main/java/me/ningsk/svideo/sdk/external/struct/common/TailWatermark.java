package me.ningsk.svideo.sdk.external.struct.common;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 36<br>
 * 版本：v1.0<br>
 */
public class TailWatermark
{
    private String mImgPath;
    private float mSizeX;
    private float mSizeY;
    private float mPosX;
    private float mPosY;
    private long mDuration = 3000L;

    public String getImgPath() {
        return this.mImgPath;
    }

    public void setImgPath(String imgPath) {
        this.mImgPath = imgPath;
    }

    public float getSizeX() {
        return this.mSizeX;
    }

    public void setSizeX(float sizeX) {
        this.mSizeX = sizeX;
    }

    public float getSizeY() {
        return this.mSizeY;
    }

    public void setSizeY(float sizeY) {
        this.mSizeY = sizeY;
    }

    public float getPosX() {
        return this.mPosX;
    }

    public void setPosX(float posX) {
        this.mPosX = posX;
    }

    public float getPosY() {
        return this.mPosY;
    }

    public void setPosY(float postY) {
        this.mPosY = postY;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }
}
