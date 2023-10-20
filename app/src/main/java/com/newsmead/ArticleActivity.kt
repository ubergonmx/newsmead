package com.newsmead

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.newsmead.databinding.ActivityArticleBinding
import com.newsmead.fragments.article.ArticleFragment

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for ArticleActivity
        val binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getStringExtra("articleId")

        if (articleId != null) {
            val articleFragment = ArticleFragment()
            val args = Bundle()
            args.putString("articleId", articleId)
            articleFragment.arguments = args

            // Replace the fragment_container view with the ArticleFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.flMainArticleContainer, articleFragment)
                .commit()
        }
    }
}