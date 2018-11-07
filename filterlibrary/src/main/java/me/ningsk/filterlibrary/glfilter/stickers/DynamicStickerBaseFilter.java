package me.ningsk.filterlibrary.glfilter.stickers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.filterlibrary.glfilter.base.GLImageAudioFilter;
import me.ningsk.filterlibrary.glfilter.stickers.bean.DynamicSticker;

/**
 * <p>描述：贴纸滤镜基类<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 56<br>
 * 版本：v1.0<br>
 */
public class DynamicStickerBaseFilter extends GLImageAudioFilter {

    // 贴纸数据
    protected DynamicSticker mDynamicSticker;

    // 贴纸加载器列表
    protected List<DynamicStickerLoader> mStickerLoaderList;

    public DynamicStickerBaseFilter(Context context, DynamicSticker sticker, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
        mDynamicSticker = sticker;
        mStickerLoaderList = new ArrayList<>();
    }

}
