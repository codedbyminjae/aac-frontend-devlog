package com.example.aac.domain.model

import android.graphics.drawable.Icon

data class Category(
    val id: String,
    val name: String,
    val iconUrl: String?,
    val displayOrder: Int,
    val iconKey: String? = null
)