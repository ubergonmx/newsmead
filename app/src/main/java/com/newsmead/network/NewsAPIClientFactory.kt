package com.newsmead.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsAPIClientFactory {
    private const val BASE_URL = "https://newsapi.org/v2/"

    fun create(): NewsAPIClient {
        val okHttpClient = OkHttpClient.Builder()
        // Add any additional OkHttpClient configuration here (e.g., authentication)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(NewsAPIClient::class.java)
    }
}
