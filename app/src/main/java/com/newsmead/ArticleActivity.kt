package com.newsmead

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.newsmead.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for ArticleActivity
        val binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}