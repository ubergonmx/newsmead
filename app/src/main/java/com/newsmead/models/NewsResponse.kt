package com.newsmead.models

data class NewsResponse (
    val status: String,
    val totalResults: Int,
    val articles: List<Article> = emptyList()
)