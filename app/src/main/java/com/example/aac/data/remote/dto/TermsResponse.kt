package com.example.aac.data.remote.dto

data class TermsResponse(
    val id: String,
    val title: String,
    val content: String,
    val isRequired: Boolean,
    val order: Int
)


