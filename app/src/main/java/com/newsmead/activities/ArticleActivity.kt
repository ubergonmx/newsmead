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
//    val args: ArticleActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for ArticleActivity
        val binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ArticleActivity", "onCreate: ArticleActivity started")


        // Receive articleId from MainActivity
        val articleId = intent.getStringExtra("articleId")

        Log.d("ArticleActivity", "onCreate: articleId: $articleId")

        // Receive articleId from navigation
//        val articleId = args.articleId

        // Update fragment with articleId
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.flMainArticleContainer) as NavHostFragment
        val navController = navHostFragment.navController

        // Retrieve article data
        val articleTitle = "TEST"
        val articleSource = "Article Source"
        val articleDate = "Article Date" // No article date on article?
        val articleReadTime = 0
        val articleImage = "https://live.staticflickr.com/7221/7251835204_89c12bd6ef_b.jpg"
        val articleBody = "Article Content"

        // Creates a "safeargs" bundle
        val bundle = bundleOf(
            "articleId" to articleId,
            "articleTitle" to articleTitle,
            "articleSource" to articleSource,
            "articleImage" to articleImage,
            "articleBody" to articleBody,
            "articleReadTime" to articleReadTime
        )

        // Clear fragment backstack (this is because ArticleActivity starts with a fragment)
        // due to nav_article_graph.xml having a startDestination.
        navController.popBackStack()

        // Navigate to article fragment using bundle
        Log.d("ArticleActivity", "onCreate: Navigating to article fragment")
        navController.navigate(R.id.articleFragment, bundle)
    }
}