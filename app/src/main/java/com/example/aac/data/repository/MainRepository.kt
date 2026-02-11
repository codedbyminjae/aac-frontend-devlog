package com.example.aac.data.repository

import com.example.aac.data.remote.api.RetrofitInstance
import com.example.aac.data.remote.dto.CategoryResponseItem
import com.example.aac.data.remote.dto.MainWordItem // ✅ 새로 만든 DTO

class MainRepository {
    // 반환 타입이 반드시 List<CategoryResponseItem> 이어야 합니다!
    suspend fun getCategories(): List<CategoryResponseItem> {
        return try {
            val response = RetrofitInstance.api.getCategories()
            if (response.success && response.data != null) {
                response.data // ✅ 여기서 리스트(List)를 반환해야 ViewModel에서 map이 가능함
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getWords(categoryId: String? = null, onlyFavorite: Boolean = false): List<MainWordItem> {
        return try {
            val response = RetrofitInstance.api.getWords(categoryId, if(onlyFavorite) true else null)

            // 서버에서 받은 List<Word>를 List<MainWordItem>으로 변환!
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