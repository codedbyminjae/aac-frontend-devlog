package com.example.aac.data.repository

import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.CategoryResponse
import com.example.aac.data.remote.dto.MainWordItem

class MainRepository {
    suspend fun getCategories(): List<CategoryResponse> {
        return try {
            val response = RetrofitInstance.api.getCategories()

            if (response.success && response.data != null) {
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getWords(categoryId: String? = null, onlyFavorite: Boolean = false): List<MainWordItem> {
        return try {
            val response = RetrofitInstance.api.getWords(categoryId, if(onlyFavorite) true else null)

            response.data.words.map { oldWord ->
                MainWordItem(
                    cardId = oldWord.cardId,

                    // 🔥 [해결] String? -> String 타입 불일치 해결
                    categoryId = oldWord.categoryId ?: "",

                    partOfSpeech = oldWord.partOfSpeech,
                    word = oldWord.word,
                    imageUrl = oldWord.imageUrl ?: "",

                    // 🛡️ [안전 장치] 다른 필드들도 null일 경우를 대비해 기본값 설정
                    isDefault = oldWord.isDefault ?: false,
                    isFavorite = oldWord.isFavorite,
                    displayOrder = oldWord.displayOrder ?: 0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}