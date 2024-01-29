package com.newsmead.fragments.bottomnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper
import com.newsmead.data.DataHelper.loadArticleDataLatest
import com.newsmead.data.DataHelper.loadCategoryData
import com.newsmead.data.DataHelper.loadSourcesData
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentSearchBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleSimplifiedAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), clickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ArticleSimplifiedAdapter
    private lateinit var data: ArrayList<Article>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(
            inflater, container, false
        )

        // Change text to date today m d, y
        binding.tvSearchDate.text = DataHelper.getDateToday()
        
        val categoryData = loadCategoryData()
        val sourcesData = loadSourcesData()

        // Clear existing chips
        binding.cgCategory.removeAllViews()
        binding.cgSources.removeAllViews()

        // Fill with chips
        for (category in categoryData) {
            val chip = ChipSearchBinding.inflate(inflater, null, false).root
            chip.text = category

            // Launch category fragment when chip is clicked
            chip.setOnClickListener {
                // Action
                val action = SearchFragmentDirections.actionSearchFragmentToArticleCategoryFragment(
                    category
                )

                Navigation.findNavController(it).navigate(action)
            }

            binding.cgCategory.addView(chip)
        }

        for (source in sourcesData) {
            val chip = ChipSearchBinding.inflate(inflater, null, false).root
            chip.text = source

            // Launch source fragment when chip is clicked
            chip.setOnClickListener {
                // Action
                val action =
                    SearchFragmentDirections.actionSearchFragmentToArticleSearchSourceFragment(
                        source
                    )

                Navigation.findNavController(it).navigate(action)
            }

            binding.cgSources.addView(chip)
        }

        // svSearchBar setOnQueryTextListener
        binding.svSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Launch search fragment when search button is clicked and pass query using
                // safe args with argument name "searchString"
                val action = query?.let {
                    SearchFragmentDirections.actionSearchFragmentToArticleSearchFragment(
                        it
                    )
                }

                Navigation.findNavController(binding.root)
                    .navigate(action!!)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        // RecyclerView of Recent Articles
        data = loadArticleDataLatest()

        adapter = ArticleSimplifiedAdapter(data, this)
        binding.rvSearchLatestNews.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearchLatestNews.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context,
            R.drawable.line_divider
        )
        binding.rvSearchLatestNews.addItemDecoration(customDividerItemDecoration)

        // Add API here
        lifecycleScope.launch {
            DataHelper.loadArticleData(context){
                data.clear()
                data.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        return binding.root
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = SearchFragmentDirections.actionSearchFragmentToArticleActivityStart(
            article
        )

        // Navigation
        Navigation.findNavController(requireView()).navigate(action)
    }
}