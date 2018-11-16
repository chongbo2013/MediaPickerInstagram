package me.ningsk.common.media;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.text.TextUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import me.ningsk.common.logger.Logger;

public class ThumbnailUtil {
    public ThumbnailUtil() {
    }

    public static Bitmap createVideoThumbnail(String video, Matrix m) {
        if (TextUtils.isEmpty(video)) {
            return null;
        } else {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(video, 0);
            if (thumb == null) {
                return null;
            } else {
                int width = Math.min(thumb.getWidth(), thumb.getHeight());
                Bitmap result;
                if (m == null) {
                    result  = Bitmap.createBitmap(thumb, 0, 0, width, width);
                } else {
                    result = Bitmap.createBitmap(thumb, 0, 0, width, width, m, false);
                }

                thumb.recycle();
                return result;
            }
        }
    }

    public static boolean createVideoThumbnail(String video, Matrix m, String output) {
        Bitmap thumb = createVideoThumbnail(video, m);
        if (thumb == null) {
            return false;
        } else {
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(output);
            } catch (FileNotFoundException e) {
                Logger.getDefaultLogger().d("DraftUtilserror creating thumbnail file " + e.getMessage(), new Object[0]);
                thumb.recycle();
                return false;
            }

            boolean succ = thumb.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            thumb.recycle();
            return succ;
        }
    }
}

