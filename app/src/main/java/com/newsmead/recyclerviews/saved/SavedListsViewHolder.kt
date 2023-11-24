package com.newsmead.recyclerviews.saved

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
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
            // Apply Style ListCardStyle.ReadLater
            this.viewBinding.cvListCard.setCardBackgroundColor(
                // Set to theme's ?cardBackgroundColorPrimary
                this.viewBinding.cvListCard.context.getColor(R.color.gold_20)
            )

            this.viewBinding.cvListCard.strokeColor =
                this.viewBinding.cvListCard.context.getColor(R.color.gold_200)

            // Change icon color
            this.viewBinding.ivListIcon.setColorFilter(
                this.viewBinding.ivListIcon.context.getColor(R.color.cobalt_100)
            )

            this.viewBinding.ivListIcon.setImageResource(R.drawable.star_filled_weight400)

        } else if (savedList.id == "offlineArticles"){
            // Apply Style ListCardStyle.OfflineArticles
            this.viewBinding.cvListCard.setCardBackgroundColor(
                // Set to theme's ?lowBackground
                this.viewBinding.cvListCard.context.getColor(R.color.gray_300)
            )

            this.viewBinding.cvListCard.strokeColor =
                this.viewBinding.cvListCard.context.getColor(R.color.gray_200)

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