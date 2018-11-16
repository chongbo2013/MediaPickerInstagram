package me.ningsk.svideo.sdk.internal.common.project;

import java.io.Serializable;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 26<br>
 * 版本：v1.0<br>
 */
public class Frame
        implements Serializable
{
    private static final long serialVersionUID = 3336548451326761341L;
    public float time;
    public int pic;
    public float alpha;

    public float getTime()
    {
        return this.time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getPic() {
        return this.pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
