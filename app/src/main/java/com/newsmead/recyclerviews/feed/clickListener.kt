package com.newsmead.recyclerviews.feed

/**
 * Interface for click listeners. This is to be used in the ArticleAdapter.
 * Helps with navigation to the ArticleFragment.
 */
interface clickListener {
    fun onItemClicked(
        articleId: String,
        articleTitle: String,
        articleSource: String,
        articleImage: String,
        articleReadTime: Int
    )
}