package me.ningsk.common.utils;

import android.content.Context;

public class DensityUtil
{
    public static int dip2px(Context context, float dp)
    {
        return (int)(0.5F + dp * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dip(Context context, float px) {
        return (int)(0.5F + px / context.getResources().getDisplayMetrics().density);
    }

    public static int sp2px(Context context, float spValue)
    {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    public static int dip2px(float value) {
        return (int)(0.5F + value * MySystemParams.getInstance().scale);
    }

    public static int px2dip(float value) {
        return (int)(0.5F + value / MySystemParams.getInstance().scale);
    }

    public static int sp2px(float value) {
        return (int)(0.5F + value * MySystemParams.getInstance().fontScale);
    }

    public static int getActualScreenWidth() {
        int width = 0;
        MySystemParams systemparams = MySystemParams.getInstance();
        if (systemparams.screenOrientation == 2)
            width = systemparams.screenHeight;
        else {
            width = systemparams.screenWidth;
        }
        return width;
    }

    public static int getActualScreenHeight() {
        int height = 0;
        MySystemParams systemparams = MySystemParams.getInstance();
        if (systemparams.screenOrientation == 2)
            height = systemparams.screenWidth;
        else {
            height = systemparams.screenHeight;
        }
        return height;
    }
}