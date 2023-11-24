package com.newsmead.recyclerviews.saved

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.newsmead.R
import com.newsmead.databinding.ItemSavedListBinding
import com.newsmead.models.SavedList

class SavedListsViewHolder(private val viewBinding: ItemSavedListBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(savedList: SavedList) {
        this.viewBinding.tvListTitle.text = savedList.title
        val articleNumber = savedList.numArticles.toString() + " articles"
        this.viewBinding.tvListNumArticles.text = articleNumber
        // Special icon for Read Later list
        if (savedList.id == "readLater") {
            val cardColor = MaterialColors.getColor(this.viewBinding.cvListCard, R.attr.listReadLaterBackground)
            this.viewBinding.cvListCard.setCardBackgroundColor(cardColor)

            val strokeColor = MaterialColors.getColor(this.viewBinding.cvListCard, R.attr.listReadLaterStroke)
            this.viewBinding.cvListCard.strokeColor = strokeColor

            // Change icon color
            this.viewBinding.ivListIcon.setColorFilter(
                this.viewBinding.ivListIcon.context.getColor(R.color.cobalt_100)
            )

            this.viewBinding.ivListIcon.setImageResource(R.drawable.star_filled_weight400)

        } else if (savedList.id == "offlineArticles"){
            val cardColor = MaterialColors.getColor(this.viewBinding.cvListCard, R.attr.listOfflineBackground)
            this.viewBinding.cvListCard.setCardBackgroundColor(cardColor)

            val strokeColor = MaterialColors.getColor(this.viewBinding.cvListCard, R.attr.listOfflineStroke)
            this.viewBinding.cvListCard.strokeColor = strokeColor

            // Change icon color
            this.viewBinding.ivListIcon.setColorFilter(
                this.viewBinding.ivListIcon.context.getColor(R.color.cobalt_100)
            )

            this.viewBinding.ivListIcon.setImageResource(R.drawable.star_filled_weight400)
        }
    }

    // Gets the cardview to add an onClickListener
    fun getCardView() = this.viewBinding.cvListCard
}