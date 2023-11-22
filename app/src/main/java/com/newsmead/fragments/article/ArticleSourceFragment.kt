package com.newsmead.fragments.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentArticleSourceBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener

class ArticleSourceFragment: Fragment(), clickListener {
    private lateinit var binding: FragmentArticleSourceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleSourceBinding.inflate(inflater, container, false)

        val args: ArticleSourceFragmentArgs by navArgs()

        Log.d("ArticleSourceFragment", "onCreateView: Binding: ${binding.root}")

        // RecyclerView of Articles
        val sourceArticlesData = DataHelper.loadSourceArticlesData()
        binding.rvSourceArticles.adapter = ArticleAdapter(sourceArticlesData, this)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSourceArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvSourceArticles.addItemDecoration(customDividerItemDecoration)

        // Set ivSourceLogo
        binding.ivSourceLogo.setImageResource(R.drawable.sample_source_image)

        // Fill chips
        val chipData = DataHelper.loadCategoryData()
        binding.cgCategory.removeAllViews()
        for (category in chipData) {
            val chip = ChipSearchBinding.inflate(inflater, container, false).root
            chip.text = category
            binding.cgCategory.addView(chip)
        }

        // Back button to go back to previous fragment
        binding.btnArticleRecommendedBack.setOnClickListener {
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
        Log.d("ArticleSourceFragment", "onItemClicked: Navigating to ArticleFragment")
        // Action
        val action = ArticleSourceFragmentDirections.actionArticleSourceFragmentToArticleFragment(
            articleId,
            articleTitle,
            articleSource,
            articleImage,
            "Article Content",
            articleReadTime
        )

        Navigation.findNavController(requireActivity(), R.id.flMainArticleContainer)
            .navigate(action)
    }
}