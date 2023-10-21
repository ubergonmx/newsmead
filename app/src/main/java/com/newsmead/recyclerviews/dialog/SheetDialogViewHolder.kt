package com.newsmead.recyclerviews.dialog

import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemDialogListItemBinding

class SheetDialogViewHolder(private val viewBinding: ItemDialogListItemBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(title: String) {
        this.viewBinding.cbListItemSave.text = title
    }
}