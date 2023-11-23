package com.newsmead.fragments.saved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentSavedListArticlesBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SavedListArticlesFragment: Fragment(), clickListener {
    private val args: SavedListArticlesFragmentArgs by navArgs()
    private lateinit var binding: FragmentSavedListArticlesBinding
    private val articleIds = ArrayList<String>()
    val data: ArrayList<Article> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Print args
        Log.d("SavedListArticles", "ListId: ${args.listId}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedListArticlesBinding.inflate(inflater, container, false)

        val adapter = ArticleAdapter(data, this)
        binding.rvSavedArticles.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedArticles.layoutManager = layoutManager

        // Back button
        binding.btnSavedBack.setOnClickListener {
            // Simply pop the current fragment off the stack
            val navController = binding.root.findNavController()
            navController.popBackStack()
        }

        // Retrieve data from Firebase Async using coroutines
        lifecycleScope.launch {
            try {
                Log.d("SavedListArticles", "Retrieving data from Firebase for ${args.listId}")
                val ids = FirebaseHelper.getArticleIdsFromList(requireContext(), "readLater")
                articleIds.addAll(ids)

                // Uncomment this function if prefer to use Firebase as a database
                // val ids = FirebaseHelper.getArticlesFromList(requireContext(), "readLater")

                // Now that you have the articleIds, you can fetch the corresponding articles
                data.addAll(getArticlesFromIds(articleIds))

                // Notify the adapter that the dataset has changed
                Log.d("SavedListArticles", "Updating adapter with ${data.size} articles")
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }


        return binding.root
    }

    private suspend fun getArticlesFromIds(ids: List<String>): List<Article> = suspendCoroutine { continuation ->
        // Implement the logic to fetch articles from the given ids

        val articles = ArrayList<Article>()

        // Some logic to fetch articles from the given ids
        // For now using dummy data
        articles.addAll(DataHelper.loadArticleData())

        // Once you have the articles, resume the coroutine
        continuation.resume(articles)
    }

    override fun onItemClicked(
        articleId: String,
        articleTitle: String,
        articleSource: String,
        articleImage: String,
        articleReadTime: Int
    ) {
        // Action
        val action = SavedListArticlesFragmentDirections.actionSavedListArticlesFragmentToArticleActivityStart(
            articleId
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}