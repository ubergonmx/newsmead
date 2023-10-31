package com.newsmead.data

import android.util.Log
import com.newsmead.data.sources.RemoteDataSource
import com.newsmead.models.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository(private val remoteDataSource: RemoteDataSource) {

    interface DataCallback<T> {
        fun onSuccess(data: T)
        fun onError(error: Throwable)
    }

    fun getTopHeadlines(country: String, apiKey: String, callback: DataCallback<NewsResponse>) {
        remoteDataSource.getTopHeadlines(country, apiKey).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    Log.d("DataRepository", "API Success")
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        callback.onSuccess(newsResponse)
                    }
                } else {
                    Log.d("DataRepository", "API Error")
                    callback.onError(Throwable("API Error"))
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("DataRepository", "API Failure")
                callback.onError(t)
            }
        })
    }
}
