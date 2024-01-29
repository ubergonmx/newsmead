package com.newsmead.fragments.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentArticleCategoryBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch

class ArticleCategoryFragment : Fragment(), clickListener {
    private val args: ArticleCategoryFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleCategoryBinding
    private lateinit var adapter: ArticleAdapter
    private lateinit var data: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleCategoryBinding.inflate(
            inflater, container, false
        )

        // Change text of tvCategoryName to categoryName
        binding.tvCategoryName.text = args.categoryName

        //RecyclerView of Articles
        data = DataHelper.loadCategoryArticlesData()

        adapter = ArticleAdapter(data, this)
        binding.rvCategoryArticles.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvCategoryArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvCategoryArticles.addItemDecoration(customDividerItemDecoration)

        // Fill chips
        val chipData: ArrayList<String> = DataHelper.loadSourcesData()
        binding.cgCategory.removeAllViews()
        for (category in chipData) {
            val chip = ChipSearchBinding.inflate(inflater, container, false).root
            chip.text = category
            binding.cgCategory.addView(chip)

            // Set onClickListener for each chip
            chip.setOnClickListener {
                updateToSource(category)
            }
        }

        // Back button to go back to previous fragment
        binding.btnArticleRecommendedBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Add API here
        lifecycleScope.launch {
            DataHelper.loadArticleData(context) {
                data.clear()
                data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        return binding.root
    }

    fun updateToSource(source: String) {
        // Uncheck all chips except for the one that was clicked
        for (i in 0 until binding.cgCategory.childCount) {
            val chip = binding.cgCategory.getChildAt(i) as Chip
            if (chip.text != source) {
                chip.isChecked = false
            }
        }

        // Add API here
//        lifecycleScope.launch {
//            DataHelper.loadArticleData {
//                data.clear()
//                data.addAll(it)
//                adapter.notifyDataSetChanged()
//            }
//        }
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = ArticleCategoryFragmentDirections.actionArticleCategoryFragmentToArticleActivityStart(
            article
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}