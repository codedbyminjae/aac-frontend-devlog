package com.example.aac.data.api

import com.example.aac.data.model.WordResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. API 명세서
interface AacApiService {
    @GET("api/words")
    suspend fun getWords(
        @Query("onlyFavorite") onlyFavorite: Boolean = false,
        @Query("categoryId") categoryId: String? = null
    ): WordResponse
}

// 2. API 접속
object RetrofitInstance {
    private const val BASE_URL = "http://52.78.164.88:3000/"

    val api: AacApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AacApiService::class.java)
    }
}