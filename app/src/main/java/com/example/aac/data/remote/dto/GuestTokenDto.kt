package com.example.aac.data.remote.dto

data class GuestTokenDto(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int
)
