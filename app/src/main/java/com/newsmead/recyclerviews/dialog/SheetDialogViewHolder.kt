package com.newsmead.recyclerviews.dialog

import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemDialogListItemBinding
import com.newsmead.fragments.layouts.listListener
import com.newsmead.models.SavedList

class SheetDialogViewHolder(
    private val viewBinding: ItemDialogListItemBinding,
    private val listener: listListener
): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(list: SavedList) {
        this.viewBinding.cbListItemSave.text = list.title
        this.viewBinding.cbListItemSave.isChecked = list.isChecked

        this.viewBinding.cbListItemSave.setOnClickListener {
            listener.onListChecked(list.id)
        }
    }
}