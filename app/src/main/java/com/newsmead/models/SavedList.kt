package com.newsmead.models

class SavedList {
    var id: String
        private set
    var title: String
        private set
    var isChecked: Boolean = false
    var numArticles: Int = 0
    var articles: ArrayList<Article> = ArrayList()
        private set

    constructor(id: String, title: String, articles: ArrayList<Article>) {
        this.id = id
        this.title = title
        this.articles = articles
    }

    /**
     * This constructor is for holding the number of articles purely for display.
     */
    constructor(id: String, title: String, numArticles: Int) {
        this.id = id
        this.title = title
        this.numArticles = numArticles
    }

    constructor(id: String, title: String) {
        this.id = id
        this.title = title
        this.articles = ArrayList()
    }
}