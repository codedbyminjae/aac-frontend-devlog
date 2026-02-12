package com.example.aac.data.remote.api

import android.content.Context
import com.example.aac.data.local.TokenDataStore
import com.example.aac.data.local.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://52.78.164.88:3000/"

    lateinit var retrofit: Retrofit

    lateinit var tokenDataStore: TokenDataStore

    fun init(context: Context) {
        // 1. DataStore 생성 (보내주신 클래스 사용)
        tokenDataStore = TokenDataStore(context)

        // 2. Provider 생성 (DataStore 연결)
        val tokenProvider = TokenProvider(tokenDataStore)

        // 3. Interceptor 생성 (Provider 연결)
        val authInterceptor = AuthInterceptor(tokenProvider)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 4. OkHttp에 인터셉터 장착
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: AacApiService by lazy {
        if (!::retrofit.isInitialized) {
            throw IllegalStateException("Application 클래스에서 RetrofitInstance.init(context)를 꼭 호출해주세요!")
        }
        retrofit.create(AacApiService::class.java)
    }
}