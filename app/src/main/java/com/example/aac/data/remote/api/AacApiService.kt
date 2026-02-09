package com.example.aac.data.remote.api

import com.example.aac.data.remote.dto.GridSettingRequest
import com.example.aac.data.remote.dto.GridSettingResponse
import com.example.aac.data.remote.dto.WordResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface AacApiService {
    // 단어 목록 조회
    @GET("api/words")
    suspend fun getWords(): WordResponse

    // 그리드 설정 조회
    @GET("api/settings/grid")
    suspend fun getGridSetting(): GridSettingResponse

    // 그리드 설정 수정
    @PATCH("api/settings/grid")
    suspend fun updateGridSetting(
        @Body request: GridSettingRequest
    ): GridSettingResponse
}