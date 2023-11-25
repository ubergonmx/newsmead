package com.newsmead.data.sources

import com.newsmead.models.NewsResponse
import com.newsmead.network.NewsAPIClient
import retrofit2.Call

class RemoteDataSource(private val apiClient: NewsAPIClient) {
    fun getTopHeadlines(country: String, apiKey: String): Call<NewsResponse> {
        return apiClient.getTopHeadlines(country, apiKey)
    }

    fun getSearchedArticles(searchQuery: String, apiKey: String): Call<NewsResponse> {
        return apiClient.getSearchedArticles(searchQuery, apiKey)
    }

}
