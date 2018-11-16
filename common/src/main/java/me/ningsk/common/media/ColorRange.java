package me.ningsk.common.media;


public enum ColorRange
{
    UNSPECIFIED(0),
    MPEG(1),
    JPEG(2);

    public final int value;

    private ColorRange(int v) { this.value = v; }

}
