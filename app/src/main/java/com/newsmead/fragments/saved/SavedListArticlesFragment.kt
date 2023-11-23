package com.newsmead.fragments.saved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.R
import com.newsmead.data.DataHelper
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentSavedListArticlesBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch
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
        Log.d("SavedListArticles", "ListId: ${args.listId} ListName: ${args.listName}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedListArticlesBinding.inflate(inflater, container, false)

        // Update name of list
        binding.tvSavedListName.text = args.listName

        // Register popup menu
        binding.btnEdit.setOnClickListener{ v: View ->
            Log.d("SavedListArticles", "Showing popup menu")
            showMenu(v, R.menu.list_popup_menu)
        }

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
                val ids = FirebaseHelper.getArticleIdsFromList(requireContext(), args.listId)
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

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context!!, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuOptionEdit -> {
                    // Create parcel list of articles
                    val articleList = data

                    // Action
                    val action = SavedListArticlesFragmentDirections.actionSavedListArticlesFragmentToSavedListEditFragment(
                        args.listId,
                        args.listName,
                        articleList.toTypedArray()
                    )

                    // Navigate
                    Navigation.findNavController(requireView()).navigate(action)
                }
                R.id.menuOptionRename -> {
                    // Android Builder Dialog
                    val builder = android.app.AlertDialog.Builder(requireContext())
                    builder.setTitle("Rename List")

                    // Set up the input
                    val input = android.widget.EditText(requireContext())
                    builder.setView(input)

                    // Set up the buttons
                    builder.setPositiveButton("OK") { _, _ ->
                        // Update the list name
                        val newName = input.text.toString()
                        binding.tvSavedListName.text = newName

                        // Firebase rename list
                        FirebaseHelper.renameList(requireContext(), args.listId, newName)
                    }

                    builder.setNegativeButton("Cancel") { _, _ ->
                        // Do nothing
                    }

                    // Show the dialog
                    builder.show()
                }
                R.id.menuOptionDelete -> {
                    // Android Builder Dialog
                    val builder = android.app.AlertDialog.Builder(requireContext())
                    builder.setTitle("Delete List")
                    builder.setMessage("Are you sure you want to delete this list?")

                    // Set up the buttons
                    builder.setPositiveButton("OK") { _, _ ->
                        // Firebase delete list
                        // FirebaseHelper.deleteList(requireContext(), args.listId)

                        // Provide snackabr with undo
                    }
                    builder.setNegativeButton("Cancel") { _, _ ->
                        // Do nothing
                    }

                    // Show the dialog
                    builder.show()
                }
            }
            true
        }

        popup.show()
    }

    private suspend fun getArticlesFromIds(ids: List<String>): List<Article> = suspendCoroutine { continuation ->
        // Implement the logic to fetch articles from the given ids

        val articles = ArrayList<Article>()

        // Some logic to fetch articles from the given ids
        // For now using dummy data
        DataHelper.loadArticleData {
            articles.addAll(it)
        }

        // Once you have the articles, resume the coroutine
        continuation.resume(articles)
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = SavedListArticlesFragmentDirections.actionSavedListArticlesFragmentToArticleActivityStart(
            article
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}