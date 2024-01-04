package com.newsmead.data

import android.app.Application
import com.newsmead.models.Article
import com.newsmead.models.NewsArticle
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Application class to initialize the Room database. This will be called before any other
 * component is initialized.
 */
class NewsMeadApplication: Application() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        DatabaseHelper.initDatabase(this)

        // Check if logged in
        // If logged in, then preload data
        if (FirebaseHelper.getUid() != "null") {
            GlobalScope.launch {
                val pairData = FirebaseHelper.getListsAndArticles(this@NewsMeadApplication)
                PreloadedData.updateSavedData(pairData)

                // Check if missing or outdated offline articles in local database
                val offlineData: List<Article> = FirebaseHelper.getOfflineArticlesList(this@NewsMeadApplication)
                val offlineArticles: List<NewsArticle> = DatabaseHelper.getNewsArticleDao().getAllNewsArticles()

                val dataIds: List<String> = offlineData.map { it.newsId }
                val articleIds: List<String> = offlineArticles.map { it.newsId }

                // If there are missing or outdated articles, then update the local database

                // 1 day = 86400000 milliseconds
                val timeLimit = 86400000
                val curTime = System.currentTimeMillis()
                val outdatedArticles: List<NewsArticle> = offlineArticles.filter { curTime - it.lastUpdated > timeLimit }

                // Call API to get body of outdated articles

                if (dataIds != articleIds) {
                    val missingIds: List<String> = dataIds.filter { !articleIds.contains(it) }
                    val missingArticles: List<Article> = offlineData.filter { missingIds.contains(it.newsId) }

                    // Call API to get body of missing articles
                }

            }
        }
    }
}