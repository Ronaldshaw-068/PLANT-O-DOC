package com.android.lillian.plantodoc;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private int mPadding;

    public PaddingItemDecoration(int padding) {
        this.mPadding = padding;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = mPadding;
        outRect.right = mPadding;
        outRect.bottom = mPadding;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mPadding;
        }
    }
}
