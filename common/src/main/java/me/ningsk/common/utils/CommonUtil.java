package me.ningsk.common.utils;


import android.app.Activity;
import android.app.Application;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommonUtil
{
    public static boolean hasNetwork(Context context)
    {
        ConnectivityManager con = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo workinfo = con.getActiveNetworkInfo();
        if ((workinfo == null) || (!workinfo.isConnected()))
        {
            return false;
        }
        return true;
    }

    public static void deleteFileByIds(Context ctx, long[] ids)
            throws IOException
    {
        for (long id : ids)
            deleteDirectory(getAssetPackageDir(ctx, id));
    }

    public static File getResourcesUnzipPath(Context context)
    {
        File path = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if ((path != null) && (path.exists())) {
            return path;
        }
        return null;
    }

    public static File getAssetPackageDir(Context context, long id)
            throws FileNotFoundException, IllegalArgumentException
    {
        File asset_root_dir = getResourcesUnzipPath(context);

        if (asset_root_dir == null) {
            throw new FileNotFoundException();
        }

        return new File(asset_root_dir, "Shop_Effect_" + String.valueOf(id));
    }

    public static String getDatabasePath(Context context)
    {
        return Environment.getDataDirectory().getPath() + "/data/" + context
                .getPackageName() + "/mall.db";
    }

    public static boolean deleteDirectory(File dir) {
        clearDirectory(dir);
        return dir.delete();
    }

    public static void clearDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files)
            if (f.isDirectory())
                deleteDirectory(f);
            else
                f.delete();
    }

    public static boolean isReadWrite()
    {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean isReadOnly()
    {
        return Environment.getExternalStorageState().equals("mounted_ro");
    }

    public static long SDFreeSize()
    {
        if ((isReadWrite()) || (isReadOnly())) {
            StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());

            long nBlocSize = statfs.getBlockSize();

            long nAvailaBlock = statfs.getAvailableBlocks();
            long nSDFreeSize = nAvailaBlock * nBlocSize;
            return nSDFreeSize;
        }
        return -1L;
    }

    public static void fixFocusedViewLeak(Application application)
    {
        if ((Build.VERSION.SDK_INT < 15) || (Build.VERSION.SDK_INT > 23)) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager)application
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        Method focusInMethod;
        final Method finishInputLockedMethod;
        final Field mHField;
        final Field mServedViewField;
        try
        {
            mServedViewField = InputMethodManager.class.getDeclaredField("mServedView");
            mServedViewField.setAccessible(true);
            mHField = InputMethodManager.class.getDeclaredField("mServedView");
            mHField.setAccessible(true);
            finishInputLockedMethod = InputMethodManager.class.getDeclaredMethod("finishInputLocked", new Class[0]);
            finishInputLockedMethod.setAccessible(true);
            focusInMethod = InputMethodManager.class.getDeclaredMethod("focusIn", new Class[] { View.class });
            focusInMethod.setAccessible(true);
        } catch (Exception unexpected) {
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected);
            return;
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks()
        {
            public void onActivityDestroyed(Activity activity)
            {
            }

            public void onActivityStarted(Activity activity)
            {
            }

            public void onActivityResumed(Activity activity)
            {
            }

            public void onActivityPaused(Activity activity)
            {
            }

            public void onActivityStopped(Activity activity)
            {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle)
            {
            }

            public void onActivityCreated(Activity activity, Bundle savedInstanceState)
            {
                CommonUtil.ReferenceCleaner cleaner = new CommonUtil.ReferenceCleaner(inputMethodManager, mHField, mServedViewField, finishInputLockedMethod);

                View rootView = activity.getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                viewTreeObserver.addOnGlobalFocusChangeListener(cleaner);
            }
        });
    }

    public static void fixInputMethodManagerLeak(Context destContext)
    {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager)destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = { "mCurRootView", "mServedView", "mNextServedView" };
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if ((obj_get != null) && ((obj_get instanceof View))) {
                    View v_get = (View)obj_get;
                    if (v_get.getContext() == destContext) {
                        f.set(imm, null);
                    }
                    else
                    {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    static class ReferenceCleaner
            implements MessageQueue.IdleHandler, View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalFocusChangeListener
    {
        private final InputMethodManager inputMethodManager;
        private final Field mHField;
        private final Field mServedViewField;
        private final Method finishInputLockedMethod;

        ReferenceCleaner(InputMethodManager inputMethodManager, Field mHField, Field mServedViewField, Method finishInputLockedMethod)
        {
            this.inputMethodManager = inputMethodManager;
            this.mHField = mHField;
            this.mServedViewField = mServedViewField;
            this.finishInputLockedMethod = finishInputLockedMethod;
        }

        public void onGlobalFocusChanged(View oldFocus, View newFocus)
        {
            if (newFocus == null) {
                return;
            }
            if (oldFocus != null) {
                oldFocus.removeOnAttachStateChangeListener(this);
            }
            Looper.myQueue().removeIdleHandler(this);
            newFocus.addOnAttachStateChangeListener(this);
        }

        public void onViewAttachedToWindow(View v)
        {
        }

        public void onViewDetachedFromWindow(View v)
        {
            v.removeOnAttachStateChangeListener(this);
            Looper.myQueue().removeIdleHandler(this);
            Looper.myQueue().addIdleHandler(this);
        }

        public boolean queueIdle()
        {
            clearInputMethodManagerLeak();
            return false;
        }

        private void clearInputMethodManagerLeak() {
            try {
                Object lock = this.mHField.get(this.inputMethodManager);

                synchronized (lock) {
                    View servedView = (View)this.mServedViewField.get(this.inputMethodManager);
                    if (servedView != null)
                    {
                        boolean servedViewAttached = servedView.getWindowVisibility() != View.GONE;

                        if (servedViewAttached)
                        {
                            servedView.removeOnAttachStateChangeListener(this);
                            servedView.addOnAttachStateChangeListener(this);
                        }
                        else {
                            Activity activity = extractActivity(servedView.getContext());
                            if ((activity == null) || (activity.getWindow() == null))
                            {
                                this.finishInputLockedMethod.invoke(this.inputMethodManager, new Object[0]);
                            } else {
                                View decorView = activity.getWindow().peekDecorView();
                                boolean windowAttached = decorView.getWindowVisibility() != View.GONE;
                                if (!windowAttached)
                                    this.finishInputLockedMethod.invoke(this.inputMethodManager, new Object[0]);
                                else
                                    decorView.requestFocusFromTouch();
                            }
                        }
                    }
                }
            }
            catch (Exception unexpected) {
                Log.e("IMMLeaks", "Unexpected reflection exception", unexpected);
            }
        }

        private Activity extractActivity(Context context) {
            while (true) {
                if ((context instanceof Application))
                    return null;
                if ((context instanceof Activity))
                    return (Activity)context;
                if (!(context instanceof ContextWrapper)) break;
                Context baseContext = ((ContextWrapper)context).getBaseContext();

                if (baseContext == context) {
                    return null;
                }
                context = baseContext;
            }
            return null;
        }
    }
}
