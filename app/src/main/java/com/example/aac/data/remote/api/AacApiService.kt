package com.example.aac.data.remote.api
import com.example.aac.data.remote.dto.WordResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AacApiService {
    @GET("api/words")
    suspend fun getWords(
        @Query("onlyFavorite") onlyFavorite: Boolean = false,
        @Query("categoryId") categoryId: String? = null
    ): WordResponse
}