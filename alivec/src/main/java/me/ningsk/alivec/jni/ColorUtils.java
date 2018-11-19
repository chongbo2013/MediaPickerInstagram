package me.ningsk.alivec.jni;

/**
 * <p>描述：颜色格式转换的类<p>
 * 作者：ningsk<br>
 * 日期：2018/11/19 10 51<br>
 * 版本：v1.0<br>
 */
public class ColorUtils {

    /**
     * 加载所有相关链接库
     */
    static {
        System.loadLibrary("colorutils");
    }

    public static native byte[] rgb2yuvfloat(byte[] rgbs, int size, int width, int height);
}
