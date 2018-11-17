package me.ningsk.svideo.base.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import me.ningsk.svideo.base.supply.IVideoTrimmerView;

/**
 * <p>描述：裁剪区域视图<p>
 * 作者：ningsk<br>
 * 日期：2018/11/17 09 32<br>
 * 版本：v1.0<br>
 */
public class VideoTrimmerView extends FrameLayout implements IVideoTrimmerView {


    public VideoTrimmerView(@NonNull Context context) {
        super(context);
    }

    public VideoTrimmerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTrimmerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDestroy() {

    }
}

