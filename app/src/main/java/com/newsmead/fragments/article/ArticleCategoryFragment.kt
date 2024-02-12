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
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentArticleCategoryBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch
import java.util.Locale

class ArticleCategoryFragment : Fragment(), clickListener {
    private val args: ArticleCategoryFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleCategoryBinding
    private lateinit var adapter: ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleCategoryBinding.inflate(
            inflater, container, false
        )

        // Start shimmer
        binding.shimmerCategory.startShimmer()

        // Change text of tvCategoryName to categoryName
        binding.tvCategoryName.text = args.categoryName

        adapter = ArticleAdapter(arrayListOf(), this)
        binding.rvCategoryArticles.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvCategoryArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvCategoryArticles.addItemDecoration(customDividerItemDecoration)

        // Fill chips
        val chipData: ArrayList<String> = DataHelper.loadSourcesData()
        binding.cgSource.removeAllViews()
        for (source in chipData) {
            val chip = ChipSearchBinding.inflate(inflater, container, false).root
            chip.text = source
            binding.cgSource.addView(chip)

            // Set onClickListener for each chip
            chip.setOnClickListener {
                updateToSource(source)
            }
        }

        // Back button to go back to previous fragment
        binding.btnArticleRecommendedBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Add API here
        lifecycleScope.launch {
            DataHelper.loadArticleData(context, category=args.categoryName.lowercase()) {
                if(FirebaseHelper.isNetworkAvailable(requireContext())){
                    // Stop the shimmer
                    binding.shimmerCategory.stopShimmer()
                    binding.shimmerCategory.visibility = View.GONE
                    // Update the adapter with retrieved articles
                    adapter.updateData(it)
                }
            }
        }
        
        return binding.root
    }

    fun updateToSource(source: String) {
        // Uncheck all chips except for the one that was clicked
        for (i in 0 until binding.cgSource.childCount) {
            val chip = binding.cgSource.getChildAt(i) as Chip
            if (chip.text != source) {
                chip.isChecked = false
            }
        }

        lifecycleScope.launch {
            DataHelper.loadArticleData(
                context,
                category=args.categoryName.lowercase(),
                source=DataHelper.reverseSourceNameMap(source)) {
                adapter.updateData(it)
            }
        }
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