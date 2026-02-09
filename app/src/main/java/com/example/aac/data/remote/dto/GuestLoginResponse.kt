package com.example.aac.data.remote.dto

data class GuestLoginResponse(
    val success: Boolean,
    val data: GuestLoginData?,
    val message: String?
)
