package me.ningsk.utilslibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * <p>描述：设备相关工具类<p>
 * 作者：ningsk<br>
 * 日期：2018/11/13 16 45<br>
 * 版本：v1.0<br>
 */
public final class DeviceUtils {

    public static int getDeviceWidth() {
        return BaseUtils.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight(){
        return BaseUtils.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean hasAppInstalled(String pkgName) {
        try {
            BaseUtils.getContext().getPackageManager().getPackageInfo(pkgName, PackageManager.PERMISSION_GRANTED);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppRunInBackground() {
        ActivityManager activityManager = (ActivityManager) BaseUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(BaseUtils.getContext().getPackageName())) {
                // return true -> Run in background
                // return false - > Run in foreground
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }
}

