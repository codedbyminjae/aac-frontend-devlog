package com.example.aac.data.remote.api

import com.example.aac.data.remote.dto.BaseResponse
import com.example.aac.data.remote.dto.GridSettingRequest
import com.example.aac.data.remote.dto.GridSettingResponse
import com.example.aac.data.remote.dto.GuestLoginRequest
import com.example.aac.data.remote.dto.GuestLoginResponse
import com.example.aac.data.remote.dto.MyInfoResponse
import com.example.aac.data.remote.dto.WordResponse
import com.example.aac.data.remote.dto.LogoutResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AacApiService {

    // [Auth] 게스트 로그인
    @POST("api/auth/guest")
    suspend fun createGuestAccount(
        @Body request: GuestLoginRequest
    ): GuestLoginResponse

    // [Main] 단어 목록 조회
    @GET("api/words")
    suspend fun getWords(): WordResponse

    // [Setting] 그리드 설정 조회
    @GET("api/settings/grid")
    suspend fun getGridSetting(): GridSettingResponse

    // [Setting] 그리드 설정 수정
    @PATCH("api/settings/grid")
    suspend fun updateGridSetting(
        @Body request: GridSettingRequest
    ): GridSettingResponse

    // [Auth] 내 정보 조회 (로그인 상태 확인용)
    @GET("api/auth/me")
    suspend fun getMyInfo(): MyInfoResponse

    // [Auth] 로그아웃
    @POST("api/auth/logout")
    suspend fun logout(): LogoutResponse

    // [Auth] 회원탈퇴
    @DELETE("api/auth/account")
    suspend fun withdraw(): BaseResponse<Unit>
}