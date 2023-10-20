package com.newsmead.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.DataHelper.loadArticleData
import com.newsmead.recyclerviews.feed.FeedArticleAdapter
import com.newsmead.databinding.FragmentSavedListArticlesBinding
class SavedListArticlesFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSavedListArticlesBinding.inflate(inflater, container, false)

        val data = loadArticleData()
        val adapter = FeedArticleAdapter(data)
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
}