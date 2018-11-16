package me.ningsk.svideo.sdk.internal.common.project;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 49<br>
 * 版本：v1.0<br>
 */
public class StaticImage
{
    public String path;
    public float x;
    public float y;
    public long start;
    public long end;
    public float width;
    public float height;
    public float rotation;
    public boolean mirror;
    public boolean isTrack;

    public StaticImage(String path, float x, float y, long start, long end, float width, float height, float rotation, boolean mirror, boolean isTrack)
    {
        this.path = path;
        this.x = x;
        this.y = y;
        this.start = start;
        this.end = end;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.mirror = mirror;
        this.isTrack = isTrack;
    }
}
