package com.newsmead.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
class CustomDividerItemDecoration(
    context: Context?,
    @DrawableRes dividerRes: Int,
    private val excludedViewHolderClass: Class<out RecyclerView.ViewHolder> ?= null
        ) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable? = context?.let { ContextCompat.getDrawable(it, dividerRes) }
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + mDivider?.intrinsicHeight!! ?: 0
            val viewHolder = parent.getChildViewHolder(child)
            if (excludedViewHolderClass?.isInstance(viewHolder) != true) {
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }

        }
    }
}
