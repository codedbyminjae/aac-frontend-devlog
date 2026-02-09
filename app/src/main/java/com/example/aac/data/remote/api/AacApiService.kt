package com.example.aac.data.remote.api
import com.example.aac.data.remote.dto.WordResponse
import com.example.aac.data.remote.dto.GuestLoginRequest
import com.example.aac.data.remote.dto.GuestLoginResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST

interface AacApiService {

    /* ===================== Words ===================== */
    @GET("api/words")
    suspend fun getWords(
        @Query("onlyFavorite") onlyFavorite: Boolean = false,
        @Query("categoryId") categoryId: String? = null
    ): WordResponse

    /* ===================== Auth ===================== */
    // 게스트 계정 생성, 로그인 없이 바로 시작하기 버튼에서 사용
    @POST("api/auth/guest")
    suspend fun createGuestAccount(
        @Body request: GuestLoginRequest
    ): GuestLoginResponse
}
