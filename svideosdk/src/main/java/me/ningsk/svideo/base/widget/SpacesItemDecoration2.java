package me.ningsk.svideo.base.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration2 extends RecyclerView.ItemDecoration{

    private int _Space;
    private int _ThumbnailsCount;

    public SpacesItemDecoration2(int space, int thumbnailsCount) {
        _Space = space;
        _ThumbnailsCount = thumbnailsCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.left = _Space;
            outRect.right = 0;
        } else if (_ThumbnailsCount > 10 && position == _ThumbnailsCount - 1) {
            outRect.left = 0;
            outRect.right = _Space;
        } else {
            outRect.left = 0;
            outRect.right = 0;
        }
    }
}
