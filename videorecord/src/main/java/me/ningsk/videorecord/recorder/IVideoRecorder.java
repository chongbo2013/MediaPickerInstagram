package me.ningsk.videorecord.recorder;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;

import java.io.File;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 09 18<br>
 * 版本：<br>
 */
public interface IVideoRecorder {

    void startPreview();

    void startRecord();

    void stopRecord();

    CameraController getCameraController();

    /**
     * @return 是否开启录制
     */
    boolean isRecordEnable();

    /**
     * @return 混合器是否正在运行
     */
    boolean isMuxerRunning();

    void onSizeChanged(int width, int height);

    void stopPreview();

    void release();

    File getOutputFile();

    /**
     * @return 获取预览的纹理
     */
    SurfaceTexture getPreviewTexture();


    /**
     * 拍照
     */
    void takePicture(TakePictureCallback callback);


    interface TakePictureCallback {

        /**
         * @param picture 图片文件
         *                在UI线程调用
         */
        void onPictureTaken(File picture);

        /**
         * @param bitmap bitmap
         * @return File 返回一个图片存储地址 , 如果返回null , 那么表示不需要保存为文件, 将不会调用 onPictureTaken
         * 此方法工作在后台线程
         */
        File onPictureTaken(Bitmap bitmap);

    }
}

