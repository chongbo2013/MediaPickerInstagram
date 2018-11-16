package me.ningsk.svideo.sdk.internal.common.project;

import android.net.Uri;

import java.io.File;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 31<br>
 * 版本：v1.0<br>
 */
public class ProjectInfo
{
    private final int canvasWidth;
    private final int canvasHeight;
    private final Uri _Uri;
    private final long _DurationNano;
    private final File _ProjectFile;
    private final File _ProjectDir;
    private final int _LayoutVersion;
    private final long _TimeStamp;
    private final int _Type;

    public ProjectInfo(Project project)
    {
        this._Uri = project.getUri();
        this._ProjectFile = project.getProjectFile();
        this._ProjectDir = project.getProjectDir();
        this._DurationNano = project.getDurationNano();
        this._LayoutVersion = project.getLayoutVersion();
        this._TimeStamp = project.getTimestamp();
        this._Type = project.getType();
        this.canvasWidth = project.getCanvasWidth();
        this.canvasHeight = project.getCanvasHeight();
    }

    public int getCanvasHeight()
    {
        return this.canvasHeight;
    }

    public int getCanvasWidth() {
        return this.canvasWidth;
    }

    public Uri getUri()
    {
        return this._Uri;
    }

    public long getDurationNano() {
        return this._DurationNano;
    }

    public File getProjectFile()
    {
        return this._ProjectFile;
    }

    public File getProjectDir() {
        return this._ProjectDir;
    }

    public int getLayoutVersion() {
        return this._LayoutVersion;
    }

    public long getTimestamp() {
        return this._TimeStamp;
    }

    public int getType()
    {
        return this._Type;
    }
}
