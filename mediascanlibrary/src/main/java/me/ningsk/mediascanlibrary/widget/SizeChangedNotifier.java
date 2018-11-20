package me.ningsk.mediascanlibrary.widget;

import android.view.View;

public interface SizeChangedNotifier {

    interface Listener {
        void onSizeChanged(View view, int w, int h, int oldw, int oldh);
    }

    void setOnSizeChangedListener(Listener listener);

}
