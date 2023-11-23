package com.newsmead.models

import android.os.Parcel
import android.os.Parcelable
import com.newsmead.R

class Article: Parcelable {
    var source: String
        private set
    var sourceImage : Int
        private set
    var title: String
        private set
    var imageId: Int
        private set
    var date: String
        private set
    var readTime: String
        private set
    var url: String
        private set
    var newsId:String = "N0000"
        private set

    constructor(source: String, sourceImage: Int,
                title: String, imageId: Int, date: String, readTime: String, url: String, newsId:String) {
        this.source = source
        this.sourceImage = sourceImage
        this.title = title
        this.imageId = imageId
        this.date = date
        this.readTime = readTime
        this.url = url
        this.newsId = newsId
    }

    // Creates an article with limited information. Only source, title, date, and readTime are
    // initialized. This is meant for testing purposes.
    constructor(source: String, title: String, date: String, readTime: String, url: String) {
        this.source = source
        this.sourceImage = R.drawable.sample_source_image
        this.title = title
        this.imageId = R.drawable.sample_article_image
        this.date = date
        this.readTime = readTime
        this.url = url

        // Provide test newsId by hashing the url
        this.newsId = "T" + url.hashCode().toString()
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
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
        dest.writeInt(sourceImage)
        dest.writeString(title)
        dest.writeInt(imageId)
        dest.writeString(date)
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