package com.newsmead.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentHistoryBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleSimplifiedAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch

class HistoryFragment: Fragment(), clickListener {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: ArticleSimplifiedAdapter
    private lateinit var data: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        data = ArrayList()

        // Back Button
        binding.btnHistoryBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView
        // val data = DataHelper.loadArticleData()
        adapter = ArticleSimplifiedAdapter(data, this)
        binding.rvHistory.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager

        // Divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvHistory.addItemDecoration(customDivider)

        // Load history data
        lifecycleScope.launch {
            val historyData = FirebaseHelper.getHistory(requireContext())

            // Stop loading animation
            binding.shimmerHistory.stopShimmer()
            binding.shimmerHistory.visibility = View.GONE

            if (historyData.isNotEmpty()) {
                data.addAll(historyData)
                adapter.notifyDataSetChanged()
            }
        }


        return binding.root
    }

    override fun onItemClicked(
        article: Article
    ) {
        // Action
        val action = HistoryFragmentDirections.actionHistoryFragmentToArticleActivityStart(
            article
        )

        Navigation.findNavController(requireView()).navigate(action)
    }
}