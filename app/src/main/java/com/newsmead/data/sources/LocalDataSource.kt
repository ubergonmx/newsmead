package com.newsmead.data.sources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newsmead.models.NewsArticle
import com.newsmead.models.NewsArticleDao

@Database(entities = [NewsArticle::class], version = 1, exportSchema = false)
abstract class LocalDataSource: RoomDatabase() {
    abstract fun newsArticleDao(): NewsArticleDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(context: Context): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDataSource::class.java,
                    "news_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}