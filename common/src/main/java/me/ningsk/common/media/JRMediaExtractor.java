package me.ningsk.common.media;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.ningsk.common.global.JRTag;
import me.ningsk.common.logger.Logger;

public class JRMediaExtractor
{
    private static final String METADATA_KEY_DURATION = "duration";
    private static final String METADATA_KEY_HEIGHT = "height";
    private static final String METADATA_KEY_WIDTH = "width";
    private static final String METADATA_KEY_ROTATION = "rotation";
    private static final String TAG = "FrameExtractor";
    private final ExecutorService mExecutor;
    private final MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
    private ThumbnailPool<ShareableBitmap, Long> mBitmapPool;
    private final Canvas mCanvas = new Canvas();
    private final Rect mRect = new Rect();
    private SparseArray mMetaDataCache = new SparseArray();
    private String mVideoPath;

    public JRMediaExtractor()
    {
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    public AsyncTask<Void, Void, ShareableBitmap> newTask(Callback callback, long timestamp_nano, long offset) {
        return new Task(callback, timestamp_nano, offset).executeOnExecutor(this.mExecutor, new Void[0]);
    }

    public void beforeTask(int width, int height, int cacheSize) {
        if (this.mBitmapPool == null) {
            this.mBitmapPool = new ThumbnailPool(new BitmapAllocator(width, height), cacheSize);
            this.mRect.set(0, 0, width, height);
        }
    }

    public boolean setDataSource(String source) {
        try {
            this.mVideoPath = source;
            File f = new File(source);
            this.mRetriever.setDataSource(f.getAbsolutePath());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "failure " + source + e.getMessage() + "， file:" + this.mVideoPath);
            return false;
        }
        return true;
    }

    public long getVideoDuration()
    {
        int key = hashKey(METADATA_KEY_DURATION);
        Object result;
        if ((result = this.mMetaDataCache.get(key)) != null)
            return ((Long)result).longValue();
        if (this.mVideoPath != null) {
            String durationStr = this.mRetriever.extractMetadata(9);
            if (!TextUtils.isEmpty(durationStr)) {
                Long duration = Long.valueOf(Long.parseLong(durationStr));
                this.mMetaDataCache.put(key, duration);
                return duration.longValue();
            }
            Logger.getDefaultLogger().e("Retrieve video duration failed", new Object[0]);
            return 0L;
        }

        Logger.getDefaultLogger().e("Has no video source,so duration is 0", new Object[0]);
        return 0L;
    }

    public int getVideoRotation()
    {
        int key = hashKey(METADATA_KEY_ROTATION);
        Object result;
        if ((result = this.mMetaDataCache.get(key)) != null)
            return ((Integer)result).intValue();
        if (this.mVideoPath != null) {
            String rotationStr = this.mRetriever.extractMetadata(24);
            if (!TextUtils.isEmpty(rotationStr)) {
                Integer rotation = Integer.valueOf(Integer.parseInt(rotationStr));
                this.mMetaDataCache.put(key, rotation);
                return rotation.intValue();
            }
            Logger.getDefaultLogger().e("Retrieve video rotation failed", new Object[0]);
            return 0;
        }

        Logger.getDefaultLogger().e("Has no video source,so rotation is 0", new Object[0]);
        return 0;
    }

    public int getVideoHeight()
    {
        int key = hashKey(METADATA_KEY_HEIGHT);
        Object result;
        if ((result = this.mMetaDataCache.get(key)) != null)
            return ((Integer)result).intValue();
        if (this.mVideoPath != null) {
            String heightStr = this.mRetriever.extractMetadata(19);
            if (!TextUtils.isEmpty(heightStr)) {
                int height = Integer.parseInt(heightStr);
                this.mMetaDataCache.put(key, Integer.valueOf(height));
                return height;
            }
            Logger.getDefaultLogger().e("Retrieve video height failed", new Object[0]);
            return 0;
        }

        Logger.getDefaultLogger().e("Has no video source,so duration is 0", new Object[0]);
        return 0;
    }

    public int getVideoWidth()
    {
        int key = hashKey(METADATA_KEY_WIDTH);
        Object result;
        if ((result = this.mMetaDataCache.get(key)) != null)
            return ((Integer)result).intValue();
        if (this.mVideoPath != null) {
            String widthStr = this.mRetriever.extractMetadata(18);
            if (!TextUtils.isEmpty(widthStr)) {
                int width = Integer.parseInt(widthStr);
                this.mMetaDataCache.put(key, Integer.valueOf(width));
                return width;
            }
            Logger.getDefaultLogger().e("Retrieve video duration failed", new Object[0]);
            return 0;
        }

        Logger.getDefaultLogger().e("Has no video source,so duration is 0", new Object[0]);
        return 0;
    }

    public int getFrameRate()
    {
        String frameStr = this.mRetriever.extractMetadata(25);
        if (!TextUtils.isEmpty(frameStr)) {
            try {
                return Integer.parseInt(frameStr);
            }
            catch (Exception e) {
                return 0;
            }
        }
        Log.e(JRTag.TAG, "Retrieve video frame failed");
        return 0;
    }

    public int getRotation()
    {
        String rotationStr = this.mRetriever.extractMetadata(24);
        if (!TextUtils.isEmpty(rotationStr)) {
            try {
                return Integer.parseInt(rotationStr);
            }
            catch (Exception e) {
                return 0;
            }
        }
        Logger.getDefaultLogger().e("Retrieve video frame failed", new Object[0]);
        return 0;
    }

    public void release()
    {
        this.mExecutor.shutdownNow();
        while (true) {
            try {
                if (this.mExecutor.awaitTermination(1L, TimeUnit.SECONDS))
                    break;
            }
            catch (InterruptedException e)
            {
            }
        }
        this.mRetriever.release();

        this.mBitmapPool.release();
    }

    private int hashKey(String metaDataKey)
    {
        return (metaDataKey + this.mVideoPath).hashCode();
    }

    private class Task extends AsyncTask<Void, Void, ShareableBitmap>
    {
        private final long mTimestampNano;
        private final long mQuanOffset;
        private JRMediaExtractor.Callback mCallback;

        public Task(JRMediaExtractor.Callback callback, long timestamp_nano, long offset)
        {
            this.mCallback = callback;
            this.mTimestampNano = timestamp_nano;
            this.mQuanOffset = offset;
        }

        protected ShareableBitmap doInBackground(Void[] params)
        {
            if (isCancelled()) {
                return null;
            }
            long micro = TimeUnit.NANOSECONDS.toMicros(this.mTimestampNano);
            long offset = TimeUnit.NANOSECONDS.toMicros(this.mQuanOffset);
            ShareableBitmap bitmap = (ShareableBitmap)mBitmapPool.allocate(Long.valueOf(micro + offset));
            Bitmap bmp = null;
            if (!bitmap.isDataUsed()) {
                int repeat = 3;
                do {
                    bmp = fetchThumbnailFromLocal(micro);
                    micro += 100000L;
                    repeat--;
                }while ((bmp == null) && (repeat >= 0));

                if (bmp == null)
                    return null;
            }
            else {
                Log.d(TAG, "fetch thumbnail from cache time : " + micro);
                return bitmap;
            }

            if (isCancelled()) {
                return null;
            }

            JRMediaExtractor.this.mCanvas.setBitmap(bitmap.getData());
            Rect srcRect = new Rect();
            float srcRatio = bmp.getWidth() * 1.0F / bmp.getHeight();
            float dstRatio = JRMediaExtractor.this.mRect.width() * 1.0F / JRMediaExtractor.this.mRect.height();
            if (srcRatio > dstRatio) {
                srcRect.top = 0;
                srcRect.bottom = bmp.getHeight();
                srcRect.left = ((int)((bmp.getWidth() - bmp.getHeight() * dstRatio) / 2.0F));
                srcRect.right = ((int)(srcRect.left + bmp.getHeight() * dstRatio));
            } else {
                srcRect.left = 0;
                srcRect.right = bmp.getWidth();
                srcRect.top = ((int)((bmp.getHeight() - bmp.getWidth() * JRMediaExtractor.this.mRect.height() * 1.0F / JRMediaExtractor.this.mRect.width()) / 2.0F));
                srcRect.bottom = ((int)(srcRect.top + bmp.getWidth() * JRMediaExtractor.this.mRect.height() * 1.0F / JRMediaExtractor.this.mRect.width()));
            }
            JRMediaExtractor.this.mCanvas.drawBitmap(bmp, srcRect, JRMediaExtractor.this.mRect, null);

            bmp.recycle();
            return bitmap;
        }

        private Bitmap fetchThumbnailFromLocal(long micro) {
            Bitmap bmp = null;
            bmp = mRetriever.getFrameAtTime(micro);
            if (bmp == null) {
                bmp = mRetriever.getFrameAtTime(micro, 0);
            }
            if (bmp == null) {
                bmp = mRetriever.getFrameAtTime(micro, MediaMetadataRetriever.OPTION_NEXT_SYNC);
            }
            if (bmp == null) {
                Log.e(TAG, "failed to extract frame: " + this.mTimestampNano + "ns");
            }
            return bmp;
        }

        protected void onCancelled(ShareableBitmap bitmap)
        {
            if (bitmap != null)
                bitmap.release();
        }

        protected void onPostExecute(ShareableBitmap bitmap)
        {
            Log.d(TAG, "fetch thumbnail call back to user time : " + TimeUnit.NANOSECONDS.toMicros(this.mTimestampNano));
            this.mCallback.onFrameExtracted(bitmap, this.mTimestampNano);
        }
    }

    public interface Callback
    {
         void onFrameExtracted(ShareableBitmap paramShareableBitmap, long paramLong);
    }
}
