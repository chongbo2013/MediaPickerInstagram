package me.ningsk.photoselector.compress;

import android.graphics.Bitmap;

import java.io.File;

public class LuBanBuilder {
    int maxSize;

    int maxWidth;

    int maxHeight;

    File cacheDir;

    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    int gear = LuBan.THIRD_GEAR;

    LuBanBuilder(File cacheDir) {
        this.cacheDir = cacheDir;
    }
}
