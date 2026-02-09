package com.example.aac.domain.model

data class LoginState(
    val userId: String,
    val nickname: String,
    val accountType: String,   // "GUEST" 등
    val accessToken: String,
    val tokenType: String,     // "Bearer"
    val expiresIn: Int         // seconds
)
