package com.newsmead.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper.loadArticleData
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.databinding.FragmentSavedAllBinding
import com.newsmead.fragments.bottomnavigation.SavedFragmentDirections
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener

class SavedAllFragment: Fragment(), clickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSavedAllBinding.inflate(inflater, container, false)

        // Data generation
        val data = loadArticleData()

        // use feed item recyclerview adapter
        binding.rvSavedAll.adapter = ArticleAdapter(data, this)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedAll.layoutManager = layoutManager

        // Add divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvSavedAll.addItemDecoration(customDivider)


        return binding.root
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = SavedFragmentDirections.actionSavedFragmentToArticleActivityStart(
            article
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}