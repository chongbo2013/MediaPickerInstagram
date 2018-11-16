package me.ningsk.common.global;

import android.content.Context;

import me.ningsk.common.utils.SignatureUtils;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/16 09 59<br>
 * 版本：v1.0<br>
 */
public class AppInfo
{
    private static final String TAG = AppInfo.class.getName();
    private String mSignature;

    public String obtainAppSignature(Context context)
    {
        if ((this.mSignature == null) || ("".equals(this.mSignature))) {
            this.mSignature = SignatureUtils.getSingInfo(context);
        }
        return this.mSignature;
    }

    public static AppInfo getInstance()
    {
        return AppInfoHolder.sAppInfoInstance;
    }

    private static class AppInfoHolder
    {
        private static AppInfo sAppInfoInstance = new AppInfo();
    }
}