package me.ningsk.videorecord.effects;

import me.ningsk.videorecord.camera.Size;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 48<br>
 * 版本：v1.0<br>
 */
public interface VideoEffect {

    void prepare(Size size);

    int applyEffect(int fbo,int textureIdIn);

    void destroy();
}
