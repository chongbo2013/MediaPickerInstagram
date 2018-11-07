package me.ningsk.filterlibrary.glfilter.stickers;

import android.content.Context;

import me.ningsk.filterlibrary.glfilter.base.GLImageGroupFilter;
import me.ningsk.filterlibrary.glfilter.stickers.bean.DynamicSticker;
import me.ningsk.filterlibrary.glfilter.stickers.bean.DynamicStickerFrameData;
import me.ningsk.filterlibrary.glfilter.stickers.bean.DynamicStickerNormalData;

/**
 * <p>描述：动态贴纸滤镜<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 11 02<br>
 * 版本：v1.0<br>
 */
public class GLImageDynamicStickerFilter extends GLImageGroupFilter {

    public GLImageDynamicStickerFilter(Context context, DynamicSticker sticker) {
        super(context);
        if (sticker == null || sticker.dataList == null) {
            return;
        }
        // 如果存在普通贴纸数据，则添加普通贴纸滤镜
        for (int i = 0; i < sticker.dataList.size(); i++) {
            if (sticker.dataList.get(i) instanceof DynamicStickerNormalData) {
                mFilters.add(new DynamicStickerNormalFilter(context, sticker));
                break;
            }
        }
        // 判断是否存在前景贴纸滤镜
        for (int i = 0; i < sticker.dataList.size(); i++) {
            if (sticker.dataList.get(i) instanceof DynamicStickerFrameData) {
                mFilters.add(new DynamicStickerFrameFilter(context, sticker));
                break;
            }
        }
    }

}

