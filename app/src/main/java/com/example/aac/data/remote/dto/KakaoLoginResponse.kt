package com.example.aac.data.remote.dto

data class KakaoLoginResponse(
    val success: Boolean,
    val data: KakaoLoginData?,
    val message: String?
)

data class KakaoLoginData(
    val user: UserDto?,          // nullable
    val tokens: TokenDto?,       // nullable
    val pendingToken: String?,   // 추가
    val provider: String?        // optional
)


data class UserDto(
    val id: String,
    val nickname: String,
    val accountType: String
)

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int
)
