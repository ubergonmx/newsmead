package com.newsmead.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsArticleDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertNewsArticle(article: NewsArticle)

    @Query("SELECT * FROM news_articles")
    suspend fun getAllNewsArticles(): List<NewsArticle>

    @Query("SELECT * FROM news_articles WHERE newsId = :newsId")
    suspend fun getNewsArticle(newsId: String): NewsArticle

    @Query("DELETE * FROM news_articles WHERE newsId = :newsId")
    suspend fun deleteNewsArticle(newsId: String)
}