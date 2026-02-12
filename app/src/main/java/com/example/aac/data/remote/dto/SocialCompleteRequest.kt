package com.example.aac.data.remote.dto

data class SocialCompleteRequest(
    val pendingToken: String,
    val agreements: List<AgreementDto>
)
