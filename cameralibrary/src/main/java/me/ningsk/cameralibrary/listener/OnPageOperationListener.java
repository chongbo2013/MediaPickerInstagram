package me.ningsk.cameralibrary.listener;

/**
 * <p>描述：页面监听器<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 40<br>
 * 版本：v1.0<br>
 */
public interface OnPageOperationListener {

    // 打开图库页面
    void onOpenGalleryPage();

    // 打开图片编辑页面
    void onOpenImageEditPage(String path);

    // 打开视频编辑页面
    void onOpenVideoEditPage(String path);

}

