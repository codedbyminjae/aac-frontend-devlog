package com.example.aac.data.remote.dto

data class GridSettingResponse(
    val success: Boolean,
    val data: GridSettingData,
    val message: String
)

data class GridSettingData(
    val gridColumns: Int
)

// PATCH 요청 시 보낼 데이터
data class GridSettingRequest(
    val gridColumns: Int
)