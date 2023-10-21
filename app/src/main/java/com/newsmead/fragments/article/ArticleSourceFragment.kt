package com.newsmead.fragments.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.DataHelper
import com.newsmead.R
import com.newsmead.databinding.FragmentArticleSourceBinding
import com.newsmead.recyclerviews.feed.FeedArticleAdapter
import com.newsmead.recyclerviews.feed.FeedHeaderViewHolder

class ArticleSourceFragment: Fragment() {
    private lateinit var binding: FragmentArticleSourceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleSourceBinding.inflate(inflater, container, false)

        // RecyclerView of Articles
        val sourceArticlesData = DataHelper.loadSourceArticlesData()
        binding.rvRecommendedArticles.adapter = FeedArticleAdapter(sourceArticlesData)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvRecommendedArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider, FeedHeaderViewHolder::class.java)
        binding.rvRecommendedArticles.addItemDecoration(customDividerItemDecoration)

        // Fill chips
        val chipData = DataHelper.loadCategoryData()
        binding.cgCategory.removeAllViews()
        for (category in chipData) {
            val chip = inflater.inflate(R.layout.chip_search, null) as Chip
            chip.text = category
            binding.cgCategory.addView(chip)
        }

        // Back button to go back to previous fragment
        binding.btnArticleRecommendedBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        return binding.root
    }
}