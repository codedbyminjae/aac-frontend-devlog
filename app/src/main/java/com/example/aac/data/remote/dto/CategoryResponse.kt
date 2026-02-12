package com.example.aac.data.remote.dto

data class CategoryResponse(
    val success: Boolean,
    val data: List<CategoryResponseItem>?,
    val message: String
)

data class CategoryResponseItem(
    val categoryId: String,
    val name: String,
    val iconUrl: String? = null
)