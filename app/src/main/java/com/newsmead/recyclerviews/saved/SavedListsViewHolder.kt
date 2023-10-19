package com.newsmead.recyclerviews.saved

import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemSavedListBinding
class SavedListsViewHolder(private val viewBinding: ItemSavedListBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(list: List) {
        this.viewBinding.tvListTitle.text = list.title
        this.viewBinding.tvListNumArticles.text = list.numArticles.toString()
    }
}