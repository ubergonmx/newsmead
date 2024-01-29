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

        data = DataHelper.loadSourceArticlesData()

        // RecyclerView of Articles
        adapter = ArticleAdapter(data, this)
        // Load data and update the adapter when available
        DataHelper.loadSearchArticlesData (searchQuery) { articles ->
            adapter.updateData(articles)
        }

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
            bottomSheetDialogSearchFilter.show(requireActivity().supportFragmentManager, "BottomSheetDialogSearchFilter")
        }

        // Add API here
        lifecycleScope.launch {
            DataHelper.loadArticleData(context) {
                data.clear()
                data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
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