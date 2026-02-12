package com.example.aac.data.repository

import com.example.aac.data.mapper.WordMapper
import com.example.aac.data.remote.api.AacApiService
import com.example.aac.data.remote.dto.* // DTO 패키지 임포트 확인해주세요
import com.example.aac.domain.model.Category
import com.example.aac.domain.model.Word
import com.example.aac.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val api: AacApiService
) : CategoryRepository {

    // ✅ 1. 카테고리 전체 조회
    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            if (response.success && response.data != null) {
                val list = response.data.map { dto ->
                    Category(
                        id = dto.id ?: "",
                        name = dto.name,
                        displayOrder = dto.displayOrder ?: 0,
                        iconKey = dto.iconKey,
                        iconUrl = dto.iconUrl
                    )
                }
                // 화면에 보여줄 때 순서(displayOrder)대로 정렬해서 반환
                Result.success(list.sortedBy { it.displayOrder })
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ 2. 카테고리 생성
    override suspend fun createCategory(name: String, iconKey: String): Result<Category> {
        return try {
            // 생성 시에는 displayOrder가 보통 서버에서 자동 할당되거나 0으로 보냄
            val request = CreateCategoryRequest(name = name, iconKey = iconKey, iconUrl = null)
            val response = api.createCategory(request)

            if (response.success && response.data != null) {
                val data = response.data
                Result.success(
                    Category(
                        id = data.id ?: "",
                        name = data.name,
                        displayOrder = data.displayOrder ?: 0,
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

    // ✅ 3. 카테고리 수정 (여기가 문제였던 부분!)
    override suspend fun updateCategory(
        id: String,
        name: String,
        iconKey: String,
        displayOrder: Int
    ): Result<Category> {
        return try {
            // 🔥 [핵심 수정] 서버 로그에 맞춰 4가지 필드를 모두 채워서 보냅니다.
            val request = UpdateCategoryRequest(
                name = name,
                iconKey = iconKey,
                displayOrder = displayOrder,
                iconUrl = null // 서버가 이 필드를 요구하므로 명시적으로 null 전달
            )

            val response = api.updateCategory(id, request)

            if (response.success && response.data != null) {
                val data = response.data
                Result.success(
                    Category(
                        id = data.id ?: id,
                        name = data.name ?: name,
                        displayOrder = data.displayOrder ?: displayOrder,
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

    // ✅ 4. 카테고리 삭제
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

    // ✅ 5. 순서 일괄 변경
    override suspend fun updateCategoryOrders(orders: Map<String, Int>): Result<Boolean> {
        return try {
            // Map<ID, 순서> -> List<CategoryOrderItem> 변환
            val orderItems = orders.map { (id, order) ->
                // DTO에서 @SerializedName("categoryId")로 매핑해뒀으므로 id를 그대로 넘김
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

    // ✅ 6. 낱말 목록 조회
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