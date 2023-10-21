package com.newsmead.recyclerviews.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemFeedHeaderBinding

class FeedHeaderViewHolder(private val viewBinding: ItemFeedHeaderBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData() {
        // Add History Button Navigation
        viewBinding.btnHistory.setOnClickListener {
            val navController = viewBinding.root.findNavController()
            navController.navigate(com.newsmead.R.id.action_homeFragment_to_historyFragment)
        }

    }
}