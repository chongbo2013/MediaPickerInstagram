package me.ningsk.svideo.sdk.external.thumbnail;


import android.graphics.Bitmap;

import me.ningsk.svideo.sdk.external.struct.common.VideoDisplayMode;

public  interface JRIThumbnailFetcher {

    int addVideoSource(String path, long var2, long var4, long var6);

    /**
     * 添加一个视频源 -- path为开发者视频的本地路径
     * @param path
     * @return
     */
    int addVideoSource(String path);

    int addImageSource(String var1, long var2, long var4);

    int fromConfigJson(String var1);

    /**
     * 设置输出参数
     * @param width 输出宽度
     * @param height 输出高度
     * @param mode 裁剪模式（目前可忽略，填任意值，所有的都是从中间裁剪）
     * @param scaleMode 缩放模式
     * @param cacheSize 缓存大小，即缓存的缩略图数量，缓存的图片不需要重新解码取，以达到减少耗时的目的。
     * @return
     */
    int setParameters(int width, int height, CropMode mode, VideoDisplayMode scaleMode, int cacheSize);

    /**
     * 获取某个时间点的缩略图
     * @param times
     * @param listener
     * @return
     */
    int requestThumbnailImage(long[] times, OnThumbnailCompletion listener);

    /**
     * 释放
     */
    void release();

    long getTotalDuration();

    public static enum CropMode {
        Mediate(1),
        TOP(2),
        LEFT(4),
        RIGHT(8),
        BOTTOM(16);

        int value;

        private CropMode(int mode) {
            this.value = mode;
        }
    }

    public interface OnThumbnailCompletion {
        /**
         * 成功拿到图片后的回调，得到一个bitmap， 需要拿到多张如何做?就在onThumbnailReady这个回调里面再调用一次
         * requestThumbnailImage传入不一样的时间即可.
         * @param frameBitmap
         * @param time
         */
        void onThumbnailReady(Bitmap frameBitmap, long time);

        /**
         * 出错的回调
         * @param errorCode 错误码
         */
        void onError(int errorCode);
    }

}
