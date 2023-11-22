package com.newsmead.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper.loadArticleData
import com.newsmead.recyclerviews.feed.FeedArticleAdapter
import com.newsmead.databinding.FragmentSavedListArticlesBinding
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener

class SavedListArticlesFragment: Fragment(), clickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSavedListArticlesBinding.inflate(inflater, container, false)

        val data = loadArticleData()
        val adapter = ArticleAdapter(data, this)
        binding.rvSavedArticles.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedArticles.layoutManager = layoutManager

        // Back button
        binding.btnSavedBack.setOnClickListener {
            // Simply pop the current fragment off the stack
            val navController = binding.root.findNavController()
            navController.popBackStack()
        }
        return binding.root
    }

    override fun onItemClicked(
        articleId: String,
        articleTitle: String,
        articleSource: String,
        articleImage: String,
        articleReadTime: Int
    ) {
        // Action
        val action = SavedListArticlesFragmentDirections.actionSavedListArticlesFragmentToArticleActivityStart(
            articleId
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}