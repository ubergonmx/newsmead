package com.newsmead.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.databinding.FragmentHistoryBinding
import com.newsmead.recyclerviews.feed.ArticleSimplifiedAdapter
import com.newsmead.recyclerviews.feed.clickListener

class HistoryFragment: Fragment(), clickListener {
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Back Button
        binding.btnHistoryBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView
        // val data = DataHelper.loadArticleData()
        binding.rvHistory.adapter = ArticleSimplifiedAdapter(ArrayList(), this)
        // Load data and update adapter
        DataHelper.loadArticleData { articles ->
            // Stop the shimmer
            binding.shimmerHistory.stopShimmer()
            binding.shimmerHistory.visibility = View.GONE

            // Update the adapter with the data
            (binding.rvHistory.adapter as ArticleSimplifiedAdapter).updateData(articles)
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager

        // Divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvHistory.addItemDecoration(customDivider)


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
        val action = HistoryFragmentDirections.actionHistoryFragmentToArticleActivityStart(
            articleId
        )

        Navigation.findNavController(requireView()).navigate(action)
    }
}