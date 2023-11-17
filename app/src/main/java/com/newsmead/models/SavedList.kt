package com.newsmead.models

class SavedList {
    var title: String
        private set
    var articles: ArrayList<Article> = ArrayList()
        private set

    constructor(title: String, articles: ArrayList<Article>) {
        this.title = title
        this.articles = articles
    }

    constructor(title: String) {
        this.title = title
        this.articles = ArrayList()
    }
}