package me.ningsk.filterlibrary.audioplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * <p>描述：带音乐对焦的播放器<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 10<br>
 * 版本：v1.0<br>
 */
public class AutoFocusPlayer extends MediaPlayer {

    // 音频对焦管理器
    private AudioFocusManager mFocusManager;

    public AutoFocusPlayer(Context context) {
        super();
        mFocusManager = AudioFocusManager.getInstance();
        mFocusManager.init(context);
        mFocusManager.addAudioFocusChangeListener(mFocusChangeListener);
    }

    @Override
    public void start() throws IllegalStateException {
        if (mFocusManager.isFocused()) {
            super.start();
            onStart();
        } else {
            mFocusManager.requestAudioFocus();
        }
    }

    @Override
    public void release() {
        super.release();
        mFocusManager.removeAudioFocusChangeListener(mFocusChangeListener);
        onRelease();
    }

    /**
     * 开始播放
     */
    protected void onStart() {

    }

    /**
     * 释放资源
     */
    protected void onRelease() {

    }

    /**
     * 开始对焦
     */
    protected void startFocus() {
        start();
    }

    /**
     * 失去对焦
     */
    protected void lossFocus() {
        if (isPlaying()) {
            pause();
        }
    }

    /**
     * 音频对焦监听器
     */
    private AudioFocusChangeListener mFocusChangeListener = new AudioFocusChangeListener() {
        @Override
        public void onFocusChange(int state) {
            switch (state) {
                // 获得对焦
                case AudioManager.AUDIOFOCUS_GAIN:
                    startFocus();
                    break;

                // 失去对焦
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS:
                    lossFocus();
                    break;

                default:
                    break;
            }
        }
    };
}

