package me.ningsk.svideo.base.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{

    private int _HalfSpace;

    public SpacesItemDecoration(int space) {
        _HalfSpace = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getPaddingLeft() != _HalfSpace) {
            parent.setPadding(_HalfSpace, _HalfSpace, _HalfSpace, _HalfSpace);
            parent.setClipToPadding(false);
        }

        outRect.top = _HalfSpace;
        outRect.bottom = _HalfSpace;
        outRect.left = _HalfSpace;
        outRect.right = _HalfSpace;
    }
}

