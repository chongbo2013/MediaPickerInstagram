package me.ningsk.croplibrary.model;

/**
 * <p>描述：视频显示模式<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 10 52<br>
 * 版本：v1.0<br>
 */
public enum VideoDisplayMode
{
    SCALE(0),

    FILL(1);

    private int displayMode;

    private VideoDisplayMode(int mode)
    {
        this.displayMode = mode;
    }

    public int getDisplayMode() {
        return this.displayMode;
    }

    public static VideoDisplayMode valueOf(int mode) {
        return values()[mode];
    }
}
