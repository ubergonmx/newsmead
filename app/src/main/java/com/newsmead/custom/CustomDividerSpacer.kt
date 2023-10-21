package com.newsmead.custom

import androidx.recyclerview.widget.RecyclerView

/**
 * Adds spacing between items in a RecyclerView. Acts like a spacer between items.
 */
class CustomDividerSpacer(private val spacing: Int): RecyclerView.ItemDecoration() {
    public override fun getItemOffsets(
        outRect: android.graphics.Rect,
        view: android.view.View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = spacing
    }
}