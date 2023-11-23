package com.newsmead.recyclerviews.feed

import com.newsmead.models.Article

/**
 * Interface for click listeners. This is to be used in the ArticleAdapter.
 * Helps with navigation to the ArticleFragment.
 */
interface clickListener {
    fun onItemClicked(
        article: Article
    )
}