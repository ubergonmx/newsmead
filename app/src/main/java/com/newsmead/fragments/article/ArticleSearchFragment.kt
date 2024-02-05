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
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentArticleSearchBinding
import com.newsmead.fragments.layouts.BottomSheetDialogSearchFilter
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch

class ArticleSearchFragment : Fragment(), clickListener {
    private val args: ArticleSearchFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleSearchBinding
    private lateinit var adapter: ArticleAdapter
    private lateinit var data: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleSearchBinding.inflate(
            inflater, container, false
        )

        // Receive search query from SearchFragment
        val searchQuery = args.searchString

        // Change text to search query
        binding.tvSearchingForPrompt.text = searchQuery

        // Show dummy data
        data = DataHelper.loadSourceArticlesData()

        // RecyclerView of Articles
        adapter = ArticleAdapter(data, this)

        binding.rvSearchArticles.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearchArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvSearchArticles.addItemDecoration(customDividerItemDecoration)

        // Back button to go back to previous fragment
//        binding.btnArticleRecommendedBack.setOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }

        // Dialog to show when user clicks on filter
        binding.btnSearchFilter.setOnClickListener {
            val bottomSheetDialogSearchFilter = BottomSheetDialogSearchFilter()

            bottomSheetDialogSearchFilter.onApplyFiltersListener = object : BottomSheetDialogSearchFilter.OnApplyFiltersListener {
                override fun onApplyFilters(category: String?, source: String, sortBy: String, startDate: String, endDate: String) {
                    updateWithFilters(category, source, sortBy, startDate, endDate)
                }
            }
            bottomSheetDialogSearchFilter.show(requireActivity().supportFragmentManager, "BottomSheetDialogSearchFilter")
        }

        // Add API here
        lifecycleScope.launch {
            DataHelper.loadArticleData(context, searchText = searchQuery) {
                data.clear()
                data.addAll(it)
                binding.tvResultCount.text = "${it.size} results"
                adapter.notifyDataSetChanged()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun updateWithFilters(categoryVal: String?, sourceVal: String, sortByVal: String, startDateVal: String, endDateVal: String) {
        val source = DataHelper.reverseSourceNameMap(sourceVal)
        val category = if (categoryVal == "All") null else categoryVal?.lowercase()
        val sortBy = sortByVal.lowercase()
        val startDate = if (startDateVal == "") null else startDateVal
        val endDate = if (endDateVal == "") null else endDateVal

        lifecycleScope.launch {
            DataHelper.loadArticleData(
                context,
                category=category,
                source=source,
                startDate=startDate,
                endDate=endDate,
                searchText=args.searchString) {
                data.clear()
                data.addAll(it)
                binding.tvResultCount.text = "${it.size} results"
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = ArticleSearchFragmentDirections.actionArticleSearchFragmentToArticleActivityStart(
            article
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}