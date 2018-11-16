package me.ningsk.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.Window;

import java.lang.reflect.Field;

import me.ningsk.common.R;

/**
 * <p>描述：手机分辨率工具类<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 15 45<br>
 * 版本：v1.0<br>
 */
public class DensityUtil
{
    /**
     * dp转像素
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue)
    {
        return (int)(0.5F + dpValue * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 像素转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        return (int)(0.5F + pxValue / context.getResources().getDisplayMetrics().density);
    }

    /**
     * sp转像素
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue)
    {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    /**
     * dp转像素
     * @param value
     * @return
     */
    public static int dip2px(float value) {
        return (int)(0.5F + value * MySystemParams.getInstance().scale);
    }

    /**
     * 像素转dp
     * @param value
     * @return
     */
    public static int px2dip(float value) {
        return (int)(0.5F + value / MySystemParams.getInstance().scale);
    }

    /**
     * sp转像素
     * @param value
     * @return
     */
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

    /**
     * 获取真实的屏幕高度
     * @return
     */
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

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarH(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取statusbar高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }


    /**
     * 获取工具栏高度
     *
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }

    /**
     * 获取导航栏高度/虚拟按键
     *
     * @param c
     * @return
     */
    public static int getNavigationBarrH(Context c) {
        Resources resources = c.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }

    /**
     * 获取状态栏高度＋标题栏(ActionBar)高度
     * (注意，如果没有ActionBar，那么获取的高度将和上面的是一样的，只有状态栏的高度)
     *
     * @param activity
     * @return
     */
    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
    }
}