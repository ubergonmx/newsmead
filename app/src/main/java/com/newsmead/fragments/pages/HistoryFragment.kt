package com.newsmead.fragments.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.DataHelper
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.databinding.FragmentHistoryBinding
import com.newsmead.recyclerviews.feed.FeedArticleSimplifiedAdapter
import com.newsmead.recyclerviews.feed.FeedHeaderViewHolder

class HistoryFragment: Fragment() {
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
        val data = DataHelper.loadArticleData()
        binding.rvHistory.adapter = FeedArticleSimplifiedAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager

        // Divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider, FeedHeaderViewHolder::class.java)
        binding.rvHistory.addItemDecoration(customDivider)


        return binding.root
    }
}