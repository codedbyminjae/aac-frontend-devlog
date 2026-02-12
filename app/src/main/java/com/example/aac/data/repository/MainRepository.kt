package com.example.aac.data.repository

import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.CategoryResponse
import com.example.aac.data.remote.dto.MainWordItem

class MainRepository {
    suspend fun getCategories(): List<CategoryResponse> {
        return try {
            // response 타입: BaseResponse<List<CategoryResponse>>
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
                    categoryId = oldWord.categoryId,
                    partOfSpeech = oldWord.partOfSpeech,
                    word = oldWord.word,
                    imageUrl = oldWord.imageUrl,
                    isDefault = oldWord.isDefault,
                    isFavorite = oldWord.isFavorite,
                    displayOrder = oldWord.displayOrder
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}