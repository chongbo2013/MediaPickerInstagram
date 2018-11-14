package me.ningsk.utilslibrary.utils;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * <p>描述：工具类基类<p>
 * 作者：ningsk<br>
 * 日期：2018/11/13 16 27<br>
 * 版本：v1.0<br>
 */
public class BaseUtils {

    private static final String ERROR_INIT = "Initialize BaseUtils with invoke init()";

    private static WeakReference<Context> mWeakReferenceContext;

    /**
     * init in Application
     */
    public static void init(Context ctx){
        mWeakReferenceContext = new WeakReference<>(ctx);
        //something to do...
    }

    public static Context getContext() {
        if (mWeakReferenceContext == null) {
            throw new IllegalArgumentException(ERROR_INIT);
        }
        return mWeakReferenceContext.get().getApplicationContext();
    }
}
