package com.newsmead.recyclerviews.saved

import androidx.recyclerview.widget.RecyclerView
import com.newsmead.R
import com.newsmead.databinding.ItemSavedListBinding
import com.newsmead.models.SavedList

class SavedListsViewHolder(private val viewBinding: ItemSavedListBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(savedList: SavedList) {
        this.viewBinding.tvListTitle.text = savedList.title
        val articleNumber = savedList.numArticles.toString() + " articles"
        this.viewBinding.tvListNumArticles.text = articleNumber

        // Special icon for Read Later list
        if (savedList.title == "Read Later") {
            this.viewBinding.ivListIcon.setImageResource(R.drawable.star_filled_weight400)
        }
    }

    // Gets the cardview to add an onClickListener
    fun getCardView() = this.viewBinding.cvListCard
}