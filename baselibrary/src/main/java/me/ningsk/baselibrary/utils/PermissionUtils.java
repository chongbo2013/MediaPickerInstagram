package me.ningsk.baselibrary.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * <p>描述：权限请求工具类<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 18 08<br>
 * 版本：v1.0<br>
 */
public final class PermissionUtils {

    // 请求相机权限
    public static final int REQUEST_CAMERA_PERMISSION = 0x01;
    // 请求存储权限
    public static final int REQUEST_STORAGE_PERMISSION = 0x02;
    // 请求声音权限
    public static final int REQUEST_SOUND_PERMISSION = 0x03;

    private PermissionUtils() {}

    /**
     * 检查某个权限是否授权
     * @param permission
     * @return
     */
    public static boolean permissionChecking(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}

