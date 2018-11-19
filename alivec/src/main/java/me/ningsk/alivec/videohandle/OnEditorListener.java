package me.ningsk.alivec.videohandle;

/**
 * <p>描述：执行、完成、进度回调接口<p>
 * 作者：ningsk<br>
 * 日期：2018/11/19 10 55<br>
 * 版本：v1.0<br>
 */
public interface OnEditorListener {
    void onSuccess();

    void onFailure();

    void onProgress(float progress);
}

