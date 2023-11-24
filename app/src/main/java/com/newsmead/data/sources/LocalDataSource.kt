package com.newsmead.data.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newsmead.models.NewsArticle
import com.newsmead.models.NewsArticleDao

@Database(entities = [NewsArticle::class], version = 1, exportSchema = false)
abstract class LocalDataSource: RoomDatabase() {
    abstract fun newsArticleDao(): NewsArticleDao
}