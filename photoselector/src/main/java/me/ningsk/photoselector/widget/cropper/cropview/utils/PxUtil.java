package me.ningsk.photoselector.widget.cropper.cropview.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class PxUtil {

    public static int dip2px(Resources resources, int size) {
        return Math.round(TypedValue.applyDimension(1, size, resources.getDisplayMetrics()));
    }

    public static int getDip(Context context, int size) {
        return Math.round(context.getResources().getDimension(size));
    }

    public static int getDip(Resources resources, int size) {
        return Math.round(resources.getDimension(size));
    };

}
