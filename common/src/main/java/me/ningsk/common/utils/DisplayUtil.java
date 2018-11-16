package me.ningsk.common.utils;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil
{
    public static float getPixelValue(int to, DisplayMetrics dm, float value)
    {
        switch (to) {
            case 1:
                return value / dm.density;
        }
        throw new UnsupportedOperationException();
    }

    public static int getDisplayRotation(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();

        return getRotationByID(display.getRotation());
    }

    public static int getRotationByID(int id)
    {
        switch (id) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
        }
        return ((Integer)Assert.fail("invalid display rotation id: " + id)).intValue();
    }
}
