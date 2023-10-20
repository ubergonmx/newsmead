package com.newsmead.recyclerviews.saved

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.R
import com.newsmead.databinding.ItemSavedListBinding
import com.newsmead.fragments.saved.SavedListsFragment

class SavedListsViewHolder(private val viewBinding: ItemSavedListBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(savedList: SavedList) {
        this.viewBinding.tvListTitle.text = savedList.title
        this.viewBinding.tvListNumArticles.text = savedList.numArticles.toString()

        // On clicking folder button, open fragment to open saved list
        this.viewBinding.btnList.setOnClickListener {
            val listFragment = SavedListsFragment()

            // Pass data if needed
            // val bundle = Bundle()
            // bundle.putString("key", "value")
            // listFragment.arguments = bundle

            val navController = this.viewBinding.root.findNavController()
            navController.navigate(R.id.action_savedFragment_to_savedListArticlesFragment)

        }
    }
}