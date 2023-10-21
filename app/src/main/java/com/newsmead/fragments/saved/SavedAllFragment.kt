package com.newsmead.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.DataHelper.loadArticleData
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.recyclerviews.feed.FeedArticleAdapter
import com.newsmead.databinding.FragmentSavedAllBinding
import com.newsmead.recyclerviews.feed.FeedHeaderViewHolder

class SavedAllFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSavedAllBinding.inflate(inflater, container, false)

        // Data generation
        val data = loadArticleData()

        // use feed item recyclerview adapter
        binding.rvSavedAll.adapter = FeedArticleAdapter(data)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedAll.layoutManager = layoutManager

        // Add divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider, FeedHeaderViewHolder::class.java)
        binding.rvSavedAll.addItemDecoration(customDivider)


        return binding.root
    }
}