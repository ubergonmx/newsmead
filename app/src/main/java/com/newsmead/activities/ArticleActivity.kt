package com.newsmead.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.newsmead.R

import com.newsmead.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    private val args: ArticleActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for ArticleActivity
        val binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ArticleActivity", "onCreate: ArticleActivity started")

        val article = args.articleItem

        Log.d("ArticleActivity", "onCreate: articleId: ${article.newsId}")

        // Update fragment with articleId
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.flMainArticleContainer) as NavHostFragment
        val navController = navHostFragment.navController

        // Creates a "safeargs" bundle
        val bundle = bundleOf(
            "article" to article
        )

        // Clear fragment backstack (this is because ArticleActivity starts with a fragment)
        // due to nav_article_graph.xml having a startDestination.
        navController.popBackStack()

        // Navigate to article fragment using bundle
        Log.d("ArticleActivity", "onCreate: Navigating to article fragment")
        navController.navigate(R.id.articleFragment, bundle)
    }
}