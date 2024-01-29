package com.newsmead.data.sources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.newsmead.models.NewsArticle
import com.newsmead.models.NewsArticleDao

@Database(entities = [NewsArticle::class], version = 2, exportSchema = false)
abstract class LocalDataSource: RoomDatabase() {
    abstract fun newsArticleDao(): NewsArticleDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        /**
         * Migration from version 1 to 2.
         * Added last_updated column to news_article table.
         */
        private val migration1to2 = object: Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE news_article ADD COLUMN last_updated INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): LocalDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDataSource::class.java,
                    "news_database"
                ).addMigrations(migration1to2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}