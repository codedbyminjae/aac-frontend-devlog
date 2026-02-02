package com.example.aac.ui.features.category


import java.util.UUID

// 카테고리 정보를 담는 데이터 클래스
data class CategoryEditData(
    // 고유 ID: 기본값으로 랜덤 생성
    val id: String = UUID.randomUUID().toString(),

    // 아이콘 리소스 ID
    val iconRes: Int,

    // 카테고리 이름
    val title: String,

    // 포함된 낱말 개수
    val count: Int
)