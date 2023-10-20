package com.newsmead.models

class SavedList {
    var title: String
        private set
    var numArticles: Int
        private set

    constructor(title: String, numArticles: Int) {
        this.title = title
        this.numArticles = numArticles
    }

    constructor(title: String) {
        this.title = title
        this.numArticles = 0
    }
}