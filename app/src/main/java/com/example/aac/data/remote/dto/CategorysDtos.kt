package com.example.aac.data.remote.dto

import com.google.gson.annotations.SerializedName

// 통합된 카테고리 아이템 모델
data class CategoryResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("iconKey") val iconKey: String? = null,
    @SerializedName("displayOrder") val displayOrder: Int? = null,
    @SerializedName("wordCount") val wordCount: Int? = null
)

// 카테고리 생성(POST) 요청 모델
data class CreateCategoryRequest(
    @SerializedName("name") val name: String,
    @SerializedName("iconKey") val iconKey: String? = null,
    @SerializedName("iconUrl") val iconUrl: String? = null
)

// 카테고리 수정(PATCH) 요청 모델 (단일 항목 수정)
data class UpdateCategoryRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("iconKey") val iconKey: String? = null,
    @SerializedName("iconUrl") val iconUrl: String? = null,
    @SerializedName("isFavorite") val isFavorite: Boolean? = null
)

// 카테고리 삭제(DELETE) 응답 모델
data class DeleteCategoryResponse(
    @SerializedName("id") val id: String
)

// ==========================================
// 순서 변경 관련 DTO
// ==========================================

// 순서 변경 요청 Body (전체 리스트)
data class CategoryOrderRequest(
    @SerializedName("orders") val orders: List<CategoryOrderItem>
)

// 순서 변경 요청 내부 아이템 (개별 항목)
data class CategoryOrderItem(
    @SerializedName("id") val id: String,
    @SerializedName("displayOrder") val displayOrder: Int
)

// 순서 변경 응답 Data
data class CategoryOrderResponse(
    @SerializedName("updatedCount") val updatedCount: Int
)