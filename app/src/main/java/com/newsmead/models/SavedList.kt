package com.newsmead.models

class SavedList {
    var id: String
        private set
    var title: String
        private set
    var articles: ArrayList<Article> = ArrayList()
        private set

    constructor(id: String, title: String, articles: ArrayList<Article>) {
        this.id = id
        this.title = title
        this.articles = articles
    }

    constructor(id: String, title: String) {
        this.id = id
        this.title = title
        this.articles = ArrayList()
    }
}