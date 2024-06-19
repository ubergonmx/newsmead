package com.newsmead.models

import android.os.Parcel
import android.os.Parcelable
import com.newsmead.R
import org.intellij.lang.annotations.Language

class Article: Parcelable {
    var source: String
        private set
    var sourceImage: String
        private set
    var title: String
        private set
    var imageURL: String?
        private set
    var date: String
        private set
    var category: String
        private set
    var body: String = ""
        private set

    var language: String
        private set
    var readTime: String
        private set
    var url: String
        private set
    var newsId:String = "0"
        private set

    /**
     * Constructor for an article.
     * @param source The source of the article.
     * @param sourceImage The URL of the image of the source.
     * @param title The title of the article.
     * @param imageURL The URL of the image of the article.
     * @param date The date the article was published.
     * @param body The body of the article.
     * @param category The category of the article.
     * @param language The language of the article.
     * @param readTime The time it takes to read the article.
     * @param url The URL of the article.
     * @param newsId The ID of the article.
     * @return An article object.
     */
    constructor(source: String, sourceImage: String,
                title: String, imageURL: String?, date: String, body: String, category: String, language: String, readTime: String, url: String, newsId:String) {
        this.source = source
        this.sourceImage = sourceImage
        this.title = title
        this.imageURL = imageURL ?: ""
        this.date = date
        this.body = body
        this.category = category
        this.language = language
        this.readTime = readTime
        this.url = url
        this.newsId = newsId
    }

    // Creates an article with limited information. Only source, title, date, and readTime are
    // initialized. This is meant for testing purposes.
    constructor(source: String, title: String, date: String, readTime: String, url: String) {
        this.source = source
        this.sourceImage = ""
        this.title = title
        this.imageURL = ""
        this.date = date
        this.body = ""
        this.category = ""
        this.readTime = readTime
        this.language = ""
        this.url = url
        this.newsId = "0"
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(source)
        dest.writeString(sourceImage)
        dest.writeString(title)
        dest.writeString(imageURL ?: "")
        dest.writeString(date)
        dest.writeString(body)
        dest.writeString(category)
        dest.writeString(language)
        dest.writeString(readTime)
        dest.writeString(url)
        dest.writeString(newsId)
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}