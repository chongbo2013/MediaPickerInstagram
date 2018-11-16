package me.ningsk.common.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil
{
    public static void showToast(Context context, String text)
    {
        showToast(context, text, 81, 0);
    }

    public static void showToast(Context context, int rsid) {
        showToast(context, rsid, 81, 0);
    }

    public static void showToast(Context context, int rsid, int gravity, int duration) {
        showToast(context, context.getString(rsid), gravity, 0, 0, duration);
    }

    public static void showToast(Context context, String text, int gravity, int duration) {
        showToast(context, text, gravity, 0, 0, duration);
    }

    public static void showToast(Context context, String text, int gravity, int xOffset, int yOffset, int duration) {
        Toast toast = Toast.makeText(context, text, duration);

        toast.show();
    }
}
