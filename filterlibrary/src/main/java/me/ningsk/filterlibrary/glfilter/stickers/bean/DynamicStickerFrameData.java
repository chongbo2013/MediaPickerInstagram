package me.ningsk.filterlibrary.glfilter.stickers.bean;

/**
 * <p>描述：动态贴纸前景数据<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 55<br>
 * 版本：v1.0<br>
 */
public class DynamicStickerFrameData extends DynamicStickerData {
    // 对齐方式，0表示centerCrop, 1表示fitXY，2表示居中center
    public int alignMode;

    @Override
    public String toString() {
        return "DynamicStickerFrameData{" +
                "alignMode=" + alignMode +
                ", width=" + width +
                ", height=" + height +
                ", frames=" + frames +
                ", action=" + action +
                ", stickerName='" + stickerName + '\'' +
                ", duration=" + duration +
                ", stickerLooping=" + stickerLooping +
                ", audioPath='" + audioPath + '\'' +
                ", audioLooping=" + audioLooping +
                ", maxCount=" + maxCount +
                '}';
    }
}

