package me.ningsk.cameralibrary.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import me.ningsk.cameralibrary.R;
/**
 * <p>描述：通用Toolbar<p>
 * 作者：ningsk<br>
 * 日期：2018/11/13 11 33<br>
 * 版本：v1.0<br>
 */
public class ToolbarView extends RelativeLayout implements View.OnClickListener{

    private TextView mBackTextView;
    private TextView mTitleTextView;
    private TextView mNextTextView;

    private WeakReference<OnClickBackListener> mOnClickBackListener;
    private WeakReference<OnClickTitleListener> mOnClickTitleListener;
    private WeakReference<OnClickNextListener> mOnClickNextListener;

    public ToolbarView(Context context) {
        super(context);
        init(context);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.view_toolbar, this);
        mBackTextView = view.findViewById(R.id.tv_back);
        mTitleTextView = view.findViewById(R.id.tv_title);
        mNextTextView = view.findViewById(R.id.tv_next);
        mBackTextView.setOnClickListener(this);
        mTitleTextView.setOnClickListener(this);
        mNextTextView.setOnClickListener(this);
    }

    public ToolbarView setOnClickBackListener(OnClickBackListener listener) {
        this.mOnClickBackListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickTitleListener(OnClickTitleListener listener) {
        this.mOnClickTitleListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickNextListener(OnClickNextListener listener) {
        this.mOnClickNextListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setTitle(String title) {
        mTitleTextView.setText(title);
        return this;
    }

    public ToolbarView hideNext() {
        mNextTextView.setVisibility(GONE);
        return this;
    }

    public ToolbarView showNext() {
        mNextTextView.setVisibility(VISIBLE);
        return this;
    }

    public ToolbarView setNextTextColor(@ColorInt int color) {
        mNextTextView.setTextColor(color);
        return this;
    }

    @Override
    public void onClick(View v) {
       int id = v.getId();
       if (id == R.id.tv_back && mOnClickBackListener != null) {
           mOnClickBackListener.get().onClickBack();
       } else if (id == R.id.tv_title && mOnClickTitleListener != null) {
           mOnClickTitleListener.get().onClickTitle();
       } else if (id == R.id.tv_next && mOnClickNextListener != null) {
           mOnClickNextListener.get().onClickNext();
       }
    }


    public interface OnClickBackListener {
        void onClickBack();
    }

    public interface OnClickNextListener {
        void onClickNext();
    }

    public interface OnClickTitleListener {
        void onClickTitle();
    }

}
