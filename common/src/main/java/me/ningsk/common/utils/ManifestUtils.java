package me.ningsk.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ManifestUtils
{
    public static String getMetaData(Context context, String metaKey)
    {
        String name = context.getPackageName();

        String msg = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(name, PackageManager.GET_META_DATA);

            msg = appInfo.metaData.getString(metaKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(msg)) {
            return "";
        }

        return msg;
    }

    public static String getChannelNo(Context context, String channelKey)
    {
        return getMetaData(context, channelKey);
    }

    public static String getVersionName(Context context)
    {
        String version = "";

        PackageManager packageManager = context.getPackageManager();
        try
        {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(version)) {
            version = "";
        }

        return version;
    }

    public static int getVersionCode(Context context)
    {
        int versionCode = 0;

        PackageManager packageManager = context.getPackageManager();
        try
        {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }
}
