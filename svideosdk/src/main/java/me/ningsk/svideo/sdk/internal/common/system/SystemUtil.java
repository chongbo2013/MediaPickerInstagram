package me.ningsk.svideo.sdk.internal.common.system;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 17 42<br>
 * 版本：v1.0<br>
 */
public class SystemUtil {

    public static ClassLoader getCallingClassLoader() {
        try {
            Class vmClz = Class.forName("dalvik.system.VMStack");
            Method m = vmClz.getDeclaredMethod("getCallingClassLoader", new Class[0]);
            m.setAccessible(true);
            return (ClassLoader)m.invoke(null, new Object[0]);
        } catch (ClassNotFoundException e) {
            Log.e("JRLog", "Class not found!", e);
            return null;
        } catch (NoSuchMethodException e) {
            Log.d("JRLog", "No such method!", e);
            return null;
        } catch (IllegalAccessException e) {
            Log.e("JRLog", "Illegal access!", e);
            return null;
        } catch (InvocationTargetException e) {
            Log.d("JRLog", "invoke method failed", e);
        }
        return null;
    }

    public static boolean isLibraryLoaded(String libName, ClassLoader classLoader) {
        if (classLoader == null) {
            return false;
        }
        try {
            Method m = classLoader.getClass().getMethod("findLibrary", new Class[] {String.class});
            String path = (String)m.invoke(classLoader, new Object[]{libName});
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

}
