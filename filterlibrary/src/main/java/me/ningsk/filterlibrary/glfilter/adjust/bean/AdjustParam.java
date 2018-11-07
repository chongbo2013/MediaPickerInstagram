package me.ningsk.filterlibrary.glfilter.adjust.bean;

/**
 * <p>描述：调节参数<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 21<br>
 * 版本：v1.0<br>
 */
public class AdjustParam {
    // 亮度值 -1.0f ~ 1.0f
    public float brightness;
    // 对比度 0.0 ~ 4.0f
    public float contrast;
    // 曝光 -10.0f ~ 10.0f
    public float exposure;
    // 色调 0 ~ 360
    public float hue;
    // 饱和度 0 ~ 2.0f
    public float saturation;
    // 锐度 -4.0f ~ 4.0f
    public float sharpness;

    public AdjustParam() {
        brightness = 0.0f;
        contrast = 1.0f;
        exposure = 0.0f;
        hue = 0.0f;
        saturation = 1.0f;
        sharpness = 0.0f;
    }
}

