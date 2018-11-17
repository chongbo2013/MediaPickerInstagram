package me.ningsk.svideo.base.widget;

import android.view.View;

/**
 * <p>描述：尺寸变更通知<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 11 11<br>
 * 版本：v1.0<br>
 */
public interface SizeChangedNotifier {

    interface Listener {
        void onSizeChanged(View view, int w, int h, int oldw, int oldh);
    }

    void setOnSizeChangedListener(Listener listener);

}