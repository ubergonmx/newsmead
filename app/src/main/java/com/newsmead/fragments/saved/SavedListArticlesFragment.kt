package com.newsmead.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.DataHelper.loadArticleData
import com.newsmead.FeedArticleAdapter
import com.newsmead.R
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

        return binding.root
    }
}