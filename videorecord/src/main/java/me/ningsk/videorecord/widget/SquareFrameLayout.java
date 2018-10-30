package me.ningsk.videorecord.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <p>描述：正方形布局<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 10 37<br>
 * 版本：v1.0<br>
 */
public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);


    }
}
