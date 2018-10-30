package me.ningsk.videorecord.encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 08 52<br>
 * 版本：v1.0<br>
 */
public interface MediaEncoderCallBack {

    String getOutPutPath();

    void onPrepared(MediaEncoder mediaEncoder);
    void onStopped(MediaEncoder mediaEncoder);

    void sendEncodedData(int mediaTrack, ByteBuffer encodedData, MediaCodec.BufferInfo bufferInfo);

    int addMediaTrack(MediaEncoder encoder, MediaFormat format);
}
