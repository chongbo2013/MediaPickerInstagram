package me.ningsk.videorecord.recorder;

import me.ningsk.videorecord.camera.Size;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 09 14<br>
 * 版本：<br>
 */
public interface OnDrawTextureListener {

    /**
     * @param size -  the size of the frame
     */
    void onCameraStarted(Size size);


    void onCameraStopped();


    /**
     * @param FBOin 一个帧缓冲区 ，texIn 依附在这个Fbo上
     * @param texIn 包含了相机预览数据的纹理id
     * @return 应该返回一个textureId , 如果返回的textureId <= 0 , 那么将使用 texIn
     */
    int onDrawTexture(int FBOin, int texIn);

    /**
     * @param size surfaceSize
     */
    void onSizeChanged(Size size);
}
