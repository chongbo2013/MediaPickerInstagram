package me.ningsk.cameralibrary.utils;

import android.os.Environment;

import java.io.File;

/**
 * <p>描述：路径常量<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 12<br>
 * 版本：v1.0<br>
 */
public class PathConstraints {

    private PathConstraints() {}

    // 存储根目录
    private static final String StoragePath = Environment.getExternalStorageDirectory().getPath();

    // 默认相册位置
    public static final String AlbumPath = StoragePath + "/DCIM/Camera/";

    // 图片存放地址
    private static final String MediaPath = StoragePath + "/JRCamera/";

    // 是否允许录音(用户自行设置，默认开启)
    public static boolean canRecordingAudio = true;

    /**
     * 获取图片输出路径
     * @return
     */
    public static String getMediaPath() {
        String path = PathConstraints.MediaPath + "JRCamera_" + System.currentTimeMillis() + ".jpeg";
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return path;
    }

    /**
     * 获取视频输出路径
     * @return
     */
    public static String getVideoPath() {
        String path = PathConstraints.MediaPath + "JRCamera_" + System.currentTimeMillis() + ".mp4";
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return path;
    }
}

