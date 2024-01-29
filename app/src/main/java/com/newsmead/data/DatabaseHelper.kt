package com.newsmead.data

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.newsmead.data.sources.LocalDataSource

object DatabaseHelper {
    private lateinit var roomDatabase: LocalDataSource

    /**
     * Migration from version 1 to 2.
     * Added last_updated column to news_article table.
     */
    private val migration1to2 = object: Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Alters to add column last_updated
            db.execSQL("ALTER TABLE news_articles ADD COLUMN lastUpdated INTEGER NOT NULL DEFAULT 0")
        }
    }

    fun initDatabase(context: Context) {
        roomDatabase = Room.databaseBuilder(
            context.applicationContext,
            LocalDataSource::class.java, "news_database"
        ).addMigrations(migration1to2)
            .build()
    }

    fun getNewsArticleDao() = roomDatabase.newsArticleDao()
}