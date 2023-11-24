package com.newsmead.data

import android.content.Context
import androidx.room.Room
import com.newsmead.data.sources.LocalDataSource

object DatabaseHelper {
    lateinit var roomDatabase: LocalDataSource
        private set

    fun initDatabase(context: Context) {
        roomDatabase = Room.databaseBuilder(
            context.applicationContext,
            LocalDataSource::class.java, "news_database"
        ).build()
    }
}