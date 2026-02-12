package com.example.aac.data.mapper

import com.example.aac.data.remote.dto.WordResponse
import com.example.aac.domain.model.Word

object WordMapper {
    fun mapToDomain(response: WordResponse): List<Word> {
        // response.data.words는 이제 List<WordItem> 입니다.
        return response.data.words.map { dto ->
            Word(
                cardId = dto.cardId,
                word = dto.word,
                imageUrl = dto.imageUrl ?: "",
                partOfSpeech = dto.partOfSpeech,
                categoryId = dto.categoryId ?: "", // null이면 빈 문자열
                isFavorite = dto.isFavorite,

                // 🔥 [수정] null 안전 처리 추가 (Boolean? -> Boolean, Int? -> Int)
                isDefault = dto.isDefault ?: false,      // null이면 false
                displayOrder = dto.displayOrder ?: 0     // null이면 0
            )
        }
    }
}