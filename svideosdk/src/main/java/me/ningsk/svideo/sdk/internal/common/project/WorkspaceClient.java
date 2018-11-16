package me.ningsk.svideo.sdk.internal.common.project;

import android.net.Uri;

import java.io.File;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 30<br>
 * 版本：v1.0<br>
 */
public abstract interface WorkspaceClient
{
    public abstract boolean isConnected();

    public abstract boolean attachProject(ProjectInfo paramProjectInfo);

    public abstract void removeProject(Uri paramUri);

    public abstract Project createProject(int paramInt1, int paramInt2, int paramInt3);

    public abstract Project readProject(File paramFile);

    public abstract void writeProject(Project paramProject, File paramFile);

    public abstract void release();
}