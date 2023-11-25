package com.newsmead.network

import com.newsmead.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIClient {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    @GET("everything")
    fun getSearchedArticles(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}