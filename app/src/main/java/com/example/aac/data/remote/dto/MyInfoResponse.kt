package com.example.aac.data.remote.dto

data class MyInfoResponse(
    val success: Boolean,
    val data: MyInfoData?,
    val message: String?
)

data class MyInfoData(
    val id: String,
    val nickname: String,
    val email: String?,
    val accountType: String,
    val createdAt: String,
    val lastLoginAt: String,
    val settings: UserSettings,
    val subscription: UserSubscription
)

data class UserSettings(
    val ttsVoiceType: String,
    val ttsAgeType: String,
    val ttsTone: String,
    val gridColumns: Int
)

data class UserSubscription(
    val planType: String,
    val status: String,
    val startDate: String,
    val endDate: String
)
