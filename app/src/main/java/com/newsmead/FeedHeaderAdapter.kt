package com.newsmead

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemFeedHeaderBinding

class FeedHeaderAdapter: RecyclerView.Adapter<FeedHeaderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHeaderViewHolder {
        val itemViewBinding: ItemFeedHeaderBinding = ItemFeedHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val feedHeaderViewHolder = FeedHeaderViewHolder(itemViewBinding)

        return feedHeaderViewHolder
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: FeedHeaderViewHolder, position: Int) {

    }

}