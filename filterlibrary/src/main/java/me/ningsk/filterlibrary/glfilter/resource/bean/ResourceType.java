package me.ningsk.filterlibrary.glfilter.resource.bean;

/**
 * <p>描述：资源枚举类型<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 45<br>
 * 版本：v1.0<br>
 */
public enum ResourceType {

    NONE("none", -1),       // 没有资源
    STICKER("sticker", 0),  // 贴纸
    FILTER("filter", 1),    // 滤镜
    MULTI("multi", 2);      // 多种类型混合起来

    private String name;
    private int index;

    ResourceType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

