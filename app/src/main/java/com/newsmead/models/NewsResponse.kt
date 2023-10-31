package com.newsmead.models

data class NewsResponse (
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleObject> = emptyList()
)

data class ArticleObject (
    val source: SourceObject,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
) {
    class SourceObject {
        val id: String? = null
        val name: String? = null
    }
}