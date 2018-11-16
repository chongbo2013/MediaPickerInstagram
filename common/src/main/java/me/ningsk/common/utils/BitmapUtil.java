package me.ningsk.common.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil
{
    private static final String TAG = "BitmapUtil";

    public BitmapUtil() {
    }

    public static boolean writeBitmap(String path, Bitmap bitmap, int w, int h, Bitmap.CompressFormat format, int quality) {
        int org_w = bitmap.getWidth();
        int org_h = bitmap.getHeight();
        Matrix m = new Matrix();
        m.setScale((float)w / (float)org_w, (float)h / (float)org_h);
        Bitmap scale_bitmap = Bitmap.createBitmap(bitmap, 0, 0, org_w, org_h, m, true);
        boolean success = writeBitmap(path, scale_bitmap, format, quality);
        scale_bitmap.recycle();
        return success;
    }

    public static boolean writeBitmap(String path, Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(path);
        } catch (FileNotFoundException var8) {
            Log.e(TAG, "unable to open output file", var8);
            return false;
        }

        boolean success = bitmap.compress(format, quality, fout);

        try {
            fout.close();
            return success;
        } catch (IOException var7) {
            return false;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}