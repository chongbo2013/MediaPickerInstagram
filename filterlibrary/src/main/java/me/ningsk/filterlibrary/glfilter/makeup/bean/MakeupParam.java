package me.ningsk.filterlibrary.glfilter.makeup.bean;

/**
 * <p>描述：彩妆参数<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 11 04<br>
 * 版本：v1.0<br>
 */
public class MakeupParam {

    /**
     * 彩妆部位
     */
    public enum MakeupType {
        Lipstick,   // 口红
        Blush,      // 腮红
        Shadow,     // 阴影
        Eyebrow,    // 眉毛
        EyeShadow,  // 眼影
        EyeLiner,   // 眼线
        Eyelash,    // 睫毛
        Eyelids,    // 双眼皮
        Pupil,      // 瞳孔（美瞳）
    }

    // 彩妆类型
    private MakeupType type;
    // 彩妆纹理路径
    private String path;

    public MakeupParam() {

    }

    public MakeupType getType() {
        return type;
    }

    public void setType(MakeupType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

