package me.ningsk.common.view;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import me.ningsk.common.utils.DisplayUtil;

public final class ImmersiveSupport {
    public static void attachBaseContext(Activity activity, Context newBase) {
        Configuration c = newBase.getResources().getConfiguration();
        applyConfiguration(activity, c);
    }

    public static Configuration getConfiguration(Context context, Configuration c) {
        WindowManager wm = (WindowManager)context.getApplicationContext().getSystemService("window");

        DisplayMetrics dm = new DisplayMetrics();

        wm.getDefaultDisplay().getRealMetrics(dm);

        Configuration c2 = new Configuration(c);

        float width_dpf = DisplayUtil.getPixelValue(1, dm, dm.widthPixels);
        float height_dpf = DisplayUtil.getPixelValue(1, dm, dm.heightPixels);

        int width_dp = Math.round(width_dpf);
        int height_dp = Math.round(height_dpf);

        c2.screenWidthDp = width_dp;
        c2.screenHeightDp = height_dp;
        c2.smallestScreenWidthDp = Math.min(width_dp, height_dp);
        return c2;
    }

    public static void applyConfiguration(Activity activity, Configuration c) {
        Configuration c2 = getConfiguration(activity, c);
        activity.applyOverrideConfiguration(c2);
    }
}
