package com.example.aac.data.repository

import com.example.aac.data.mapper.WordMapper
import com.example.aac.data.remote.api.AacApiService
import com.example.aac.data.remote.dto.CategoryOrderItem
import com.example.aac.data.remote.dto.CategoryOrderRequest
import com.example.aac.data.remote.dto.CreateCategoryRequest
import com.example.aac.data.remote.dto.UpdateCategoryRequest
import com.example.aac.domain.model.Category
import com.example.aac.domain.model.Word
import com.example.aac.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val api: AacApiService
) : CategoryRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            if (response.success && response.data != null) {
                val list = response.data.map { dto ->
                    Category(
                        id = dto.id ?: "",
                        name = dto.name,
                        displayOrder = dto.displayOrder ?: 0,

                        // ❌ userId 삭제함 (에러 해결)

                        // ✅ [중요] 서버의 iconKey -> 도메인으로 전달
                        iconKey = dto.iconKey,

                        // ✅ [중요] iconUrl도 전달
                        iconUrl = dto.iconUrl
                    )
                }
                Result.success(list.sortedBy { it.displayOrder })
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createCategory(name: String, iconKey: String): Result<Category> {
        return try {
            val request = CreateCategoryRequest(name = name, iconKey = iconKey)
            val response = api.createCategory(request)

            if (response.success && response.data != null) {
                val data = response.data
                Result.success(
                    Category(
                        id = data.id ?: "",
                        name = data.name,
                        displayOrder = data.displayOrder ?: 0,
                        // ❌ userId 삭제함

                        iconKey = data.iconKey,
                        iconUrl = data.iconUrl
                    )
                )
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCategory(id: String, name: String, iconKey: String): Result<Category> {
        return try {
            val request = UpdateCategoryRequest(name = name, iconKey = iconKey)
            val response = api.updateCategory(id, request)

            if (response.success && response.data != null) {
                val data = response.data
                Result.success(
                    Category(
                        id = data.id ?: id,
                        name = data.name ?: name,
                        displayOrder = data.displayOrder ?: 0,
                        // ❌ userId 삭제함

                        iconKey = data.iconKey ?: iconKey,
                        iconUrl = data.iconUrl
                    )
                )
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: String): Result<String> {
        return try {
            val response = api.deleteCategory(id)
            if (response.success && response.data != null) {
                Result.success(response.data.id)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCategoryOrders(orders: Map<String, Int>): Result<Boolean> {
        return try {
            val orderItems = orders.map { (id, order) ->
                CategoryOrderItem(id = id, displayOrder = order)
            }

            val request = CategoryOrderRequest(orders = orderItems)
            val response = api.updateCategoryOrders(request)

            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWords(categoryId: String?): Result<List<Word>> {
        return try {
            val response = api.getWords(categoryId)
            if (response.success) {
                val domainList = WordMapper.mapToDomain(response)
                Result.success(domainList)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}