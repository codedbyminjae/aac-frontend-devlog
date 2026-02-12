package com.example.aac.domain.repository

import android.adservices.adid.AdId
import com.example.aac.domain.model.Category
import com.example.aac.domain.model.Word

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
    // ✅ [수정] iconKey 파라미터 추가
    suspend fun createCategory(name: String, iconKey: String): Result<Category>
    suspend fun updateCategory(id: String, name: String, iconKey: String): Result<Category>
    suspend fun deleteCategory(id: String): Result<String>
    suspend fun updateCategoryOrders(orders: Map<String, Int>): Result<Boolean>
    suspend fun getWords(categoryId: String?): Result<List<Word>>
}