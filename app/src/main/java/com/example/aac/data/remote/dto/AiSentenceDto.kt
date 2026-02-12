package com.example.aac.data.remote.dto

// 1. 요청 (Request)
data class AiPredictionRequest(
    val words: List<String>,
    val context: AiContext,
    val refresh: Boolean = false
)

data class AiContext(
    val currentTime: String,
    val previousMessages: List<String> = emptyList()
)

// 2. 응답 (Response)
data class AiPredictionResponse(
    val success: Boolean,
    val data: AiPredictionData?,
    val message: String?
)

data class AiPredictionData(
    val predictions: List<String>,
    val fromCache: Boolean
)