package com.newsmead.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper.loadArticleData
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentSavedAllBinding
import com.newsmead.fragments.bottomnavigation.SavedFragmentDirections
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedAllFragment: Fragment(), clickListener {
    private lateinit var data: ArrayList<Article>
    private lateinit var adapter: ArticleAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSavedAllBinding.inflate(inflater, container, false)

        // Data generation
        data = ArrayList()

        // use feed item recyclerview adapter
        adapter  = ArticleAdapter(data, this)
        binding.rvSavedAll.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedAll.layoutManager = layoutManager

        // Add divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvSavedAll.addItemDecoration(customDivider)

        lifecycleScope.launch(Dispatchers.IO) {
            // Update Articles Tab
            val allArticles: ArrayList<Article> = FirebaseHelper.getAllArticlesFromLists(requireContext())

            withContext(Dispatchers.Main) {
                // Update Articles Tab
                data.addAll(allArticles)
                adapter.notifyDataSetChanged()
            }
        }


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