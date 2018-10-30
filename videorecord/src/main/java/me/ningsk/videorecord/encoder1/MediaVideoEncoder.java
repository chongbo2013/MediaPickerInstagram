
package me.ningsk.videorecord.encoder1;


import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.IOException;
import java.util.Locale;

import me.ningsk.videorecord.camera.Size;
import me.ningsk.videorecord.recorder.VideoRecorder;
import me.ningsk.videorecord.util.LogUtil;

public class MediaVideoEncoder extends MediaEncoder {
    private static final boolean DEBUG = false;    // TODO set false on release
    private static final String TAG = LogUtil.TAG;

    private static final String MIME_TYPE = "video/avc";
    // parameters for recording
    private static final int FRAME_RATE = 30;
    private final Size mVideoSize;
    private final int mBitRate;
    private final int mIFrameInterval;
    private final int mFrameRate;
    private Surface mSurface;

    public MediaVideoEncoder(MediaMuxerWrapper muxer, VideoRecorder.Config config) {
        super(muxer,config);
        mVideoSize = config.getCameraController().getSurfaceSize();
        mIFrameInterval = config.getIFrameInterval();
        mBitRate = config.getVideoBitRate() <= 0 ? calcBitRate() : config.getVideoBitRate();
        mFrameRate = config.getFrameRate() <= 0 ? FRAME_RATE : config.getFrameRate();
    }

    @Override
    protected void prepare() throws IOException {
        if (DEBUG) LogUtil.logi(TAG, "prepare: ");
        mTrackIndex = -1;
        mMuxerStarted = mIsEOS = false;

        final MediaCodecInfo videoCodecInfo = selectVideoCodec(MIME_TYPE);
        if (videoCodecInfo == null) {
            LogUtil.loge(TAG, "Unable to find an appropriate codec for " + MIME_TYPE);
            return;
        }
        if (DEBUG) LogUtil.logi(TAG, "selected codec: " + videoCodecInfo.getName());

        final MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mVideoSize.getWidth(), mVideoSize.getHeight());
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);    // API >= 18
        format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, mFrameRate);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, mIFrameInterval);
        if (DEBUG) LogUtil.logi(TAG, "format: " + format);

        mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        // get Surface for encoder input
        // this method only can call between #configure and #start
        mSurface = mMediaCodec.createInputSurface();    // API >= 18
        mMediaCodec.start();
        if (DEBUG) LogUtil.logi(TAG, "prepare finishing");

    }

    public Surface getSurface() {
        return mSurface;
    }

    @Override
    public boolean frameAvailableSoon() {
        return super.frameAvailableSoon();
    }

    @Override
    protected void release() {
        if (DEBUG) LogUtil.logi(TAG, "release:");
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        super.release();
    }

    private int calcBitRate() {
        final int bitrate = mVideoSize.getWidth() * mVideoSize.getHeight() * 3 * 4;
        LogUtil.logi(TAG, String.format(Locale.getDefault(),"bitrate=%5.2f[Mbps]", bitrate / 1024f / 1024f));
        return bitrate;
    }

    /**
     * select the first codec that match a specific MIME type
     *
     * @param mimeType
     * @return null if no codec matched
     */
    protected static final MediaCodecInfo selectVideoCodec(final String mimeType) {
        if (DEBUG) LogUtil.logi(TAG, "selectVideoCodec:");

        // get the list of available codecs
        final int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (!codecInfo.isEncoder()) {    // skipp decoder
                continue;
            }
            // select first codec that match a specific MIME type and color format
            final String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    if (DEBUG)
                        LogUtil.logi(TAG, "codec:" + codecInfo.getName() + ",MIME=" + types[j]);
                    final int format = selectColorFormat(codecInfo, mimeType);
                    if (format > 0) {
                        return codecInfo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * select color format available on specific codec and we can use.
     *
     * @return 0 if no colorFormat is matched
     */
    protected static final int selectColorFormat(final MediaCodecInfo codecInfo, final String mimeType) {
        if (DEBUG) LogUtil.logi(TAG, "selectColorFormat: ");
        int result = 0;
        final MediaCodecInfo.CodecCapabilities caps;
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            caps = codecInfo.getCapabilitiesForType(mimeType);
        } finally {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        }
        int colorFormat;
        for (int i = 0; i < caps.colorFormats.length; i++) {
            colorFormat = caps.colorFormats[i];
            if (isRecognizedViewoFormat(colorFormat)) {
                if (result == 0)
                    result = colorFormat;
                break;
            }
        }
        if (result == 0)
            LogUtil.loge(TAG, "couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return result;
    }

    /**
     * color formats that we can use in this class
     */
    protected static int[] recognizedFormats;

    static {
        recognizedFormats = new int[]{
//        	MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar,
//        	MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar,
//        	MediaCodecInfo.CodecCapabilities.COLOR_QCOM_FormatYUV420SemiPlanar,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface,
        };
    }

    private static final boolean isRecognizedViewoFormat(final int colorFormat) {
        if (DEBUG) LogUtil.logi(TAG, "isRecognizedViewoFormat:colorFormat=" + colorFormat);
        final int n = recognizedFormats != null ? recognizedFormats.length : 0;
        for (int i = 0; i < n; i++) {
            if (recognizedFormats[i] == colorFormat) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void signalEndOfInputStream() {
        if (DEBUG) LogUtil.logd(TAG, "sending EOS to encoder");
        mMediaCodec.signalEndOfInputStream();    // API >= 18
        mIsEOS = true;
    }

}
