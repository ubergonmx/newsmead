package com.newsmead.data

import com.newsmead.models.Article
import com.newsmead.models.SavedList

/**
 * This singleton class is used to store data that is preloaded into the app.
 * This will help with reducing the number of database calls.
 */
object PreloadedData {
    var lists: ArrayList<SavedList> = ArrayList()
    var savedArticles: ArrayList<Article> = ArrayList()

    fun updateSavedData(pair: Pair<ArrayList<SavedList>, ArrayList<Article>>) {
        lists = pair.first
        savedArticles = pair.second
    }
}