package me.ningsk.crop;


import android.content.Context;

import me.ningsk.crop.supply.JRICrop;

public class JrCropCreator {
    private static JrCrop crop;

    public static JRICrop getCropInstance(Context context) {
        if (crop == null) {
            crop = new JrCrop(context);
        }
        return crop;
    }

    public static void destroyCropInstance() {
        if (crop != null) {
            crop.dispose();
            crop = null;
        }
    }
}
