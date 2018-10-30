package me.ningsk.videorecord.effects;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.videorecord.camera.Size;
import me.ningsk.videorecord.recorder.OnDrawTextureListener;

/**
 * <p>描述：视频特效管理器<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 11 02<br>
 * 版本：v1.0<br>
 */
public class EffectsManager implements OnDrawTextureListener {

    private final List<VideoEffect> mEffects = new ArrayList<>();

    public EffectsManager() {


    }


    public void addEffect(VideoEffect effect) {
        if (effect == null) return;
        if (mEffects.contains(effect)) return;
        mEffects.add(effect);
    }

    public void removeEffect(VideoEffect effect) {
        if (effect == null) return;
        if (mEffects.contains(effect)) mEffects.remove(effect);
    }

    @Override
    public void onCameraStarted(Size size) {
        for (VideoEffect videoEffect : mEffects) {
            videoEffect.prepare(size);
        }
    }

    @Override
    public void onCameraStopped() {
        for (VideoEffect videoEffect : mEffects) {
            videoEffect.destroy();
        }
    }

    @Override
    public int onDrawTexture(int FBOin, int texIn) {
        int textureId = texIn;
        for (VideoEffect videoEffect : mEffects) {
            textureId = videoEffect.applyEffect(FBOin, textureId);
        }
        return textureId;
    }

    @Override
    public void onSizeChanged(Size size) {
        for (VideoEffect videoEffect : mEffects) {
            videoEffect.prepare(size);
        }
    }
}

