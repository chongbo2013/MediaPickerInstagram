package me.ningsk.filterlibrary.glfilter.stickers.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>描述：动态贴纸<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 56<br>
 * 版本：v1.0<br>
 */
public class DynamicSticker {
    // 贴纸解压的文件夹路径
    public String unzipPath;
    // 贴纸列表
    public List<DynamicStickerData> dataList;

    public DynamicSticker() {
        unzipPath = null;
        dataList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "DynamicSticker{" +
                "unzipPath='" + unzipPath + '\'' +
                ", dataList=" + dataList +
                '}';
    }
}
