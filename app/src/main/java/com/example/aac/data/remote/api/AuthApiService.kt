package com.example.aac.data.remote.api

import com.example.aac.data.remote.dto.GuestLoginRequest
import com.example.aac.data.remote.dto.GuestLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/guest")
    suspend fun createGuestAccount(
        @Body request: GuestLoginRequest
    ): GuestLoginResponse
}
