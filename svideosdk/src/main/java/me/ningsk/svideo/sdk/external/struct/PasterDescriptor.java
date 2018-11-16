package me.ningsk.svideo.sdk.external.struct;

import java.util.List;

import me.ningsk.svideo.sdk.internal.common.project.Frame;
import me.ningsk.svideo.sdk.internal.common.project.FrameTime;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 46<br>
 * 版本：v1.0<br>
 */
public class PasterDescriptor
{
    public String uri;
    public String name;
    public float width;
    public float height;
    public float y;
    public float x;
    public float rotation;
    public String text;
    public String textBmpPath;
    public int textColor;
    public int preTextColor;
    public int textLabelColor;
    public long start;
    public long end;
    public long duration;
    public String font;
    public int faceId;
    public int type;
    public boolean mirror;
    public boolean hasTextLabel;
    public int maxTextSize;
    public float textHeight;
    public int textStrokeColor;
    public int preTextStrokeColor;
    public float textWidth;
    public float textOffsetX;
    public float textOffsetY;
    public long preTextBegin;
    public long preTextEnd;
    public float textRotation;
    public int kernelFrame;
    public List<Frame> frameArry;
    public List<FrameTime> timeArry;
    public boolean isTrack;
}
