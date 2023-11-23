package com.newsmead.fragments.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentArticleSearchSourceBinding
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener

class ArticleSearchSourceFragment: Fragment(), clickListener {
    private val args: ArticleSearchSourceFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleSearchSourceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleSearchSourceBinding.inflate(inflater, container, false)

        // Change text of tvSourceName to sourceName
        binding.tvSearchSourceName.text = args.sourceName

        // RecyclerView of Articles
        val sourceArticlesData = DataHelper.loadSourceArticlesData()
        binding.rvSearchSourceArticles.adapter = ArticleAdapter(sourceArticlesData, this)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearchSourceArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvSearchSourceArticles.addItemDecoration(customDividerItemDecoration)

        // Set ivSourceLogo
        binding.ivSearchSourceLogo.setImageResource(R.drawable.sample_source_image)

        // Fill chips
        val chipData = DataHelper.loadCategoryData()
        binding.cgSearchSourceCategory.removeAllViews()
        for (category in chipData) {
            val chip = ChipSearchBinding.inflate(inflater, container, false).root
            chip.text = category
            binding.cgSearchSourceCategory.addView(chip)
        }

        // Back button to go back to previous fragment
        binding.btnArticleSearchRecommendedBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
        val action = ArticleSearchSourceFragmentDirections.actionArticleSearchSourceFragmentToArticleActivityStart(
            articleId,
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}