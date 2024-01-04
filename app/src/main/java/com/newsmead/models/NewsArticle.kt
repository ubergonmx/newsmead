package com.newsmead.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for news article to be used for the Room database.
 * This will be used for offline reading.
 *
 */
@Entity(tableName = "news_articles")
data class NewsArticle (
    @PrimaryKey
    val newsId: String,
    val articleBody: String,
    val lastUpdated: Long,
)