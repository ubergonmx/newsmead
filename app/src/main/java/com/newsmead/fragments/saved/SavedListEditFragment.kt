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
import com.newsmead.databinding.FragmentSavedListEditBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SavedListEditFragment: Fragment(), clickListener {
    private val args: SavedListEditFragmentArgs by navArgs()
    private lateinit var binding: FragmentSavedListEditBinding
    private val articleIds = ArrayList<String>()
    val data: ArrayList<Article> = ArrayList()

    private val checkedIds: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Print args
        Log.d("SavedListEditArticles", "ListId: ${args.listId} ListName: ${args.listName}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedListEditBinding.inflate(inflater, container, false)

        // Update name of list
        binding.tvSavedListEditName.text = args.listName

        val adapter = ArticleAdapter(data, this)
        binding.rvSavedEditArticles.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedEditArticles.layoutManager = layoutManager

        // Back button
        binding.btnSavedEditBack.setOnClickListener {
            // Simply pop the current fragment off the stack
            val navController = binding.root.findNavController()
            navController.popBackStack()
        }

        // Populate data with articles
        val articles: ArrayList<Article> = args.listArticlesParcel?.toCollection(ArrayList()) ?: ArrayList()
        for (article in articles) {
            data.add(article)
            articleIds.add(article.newsId)
        }

        // Save button


        return binding.root
    }


    override fun onItemClicked(
        articleId: String,
        articleTitle: String,
        articleSource: String,
        articleImage: String,
        articleReadTime: Int
    ) {
        // Checkbox
    }
}