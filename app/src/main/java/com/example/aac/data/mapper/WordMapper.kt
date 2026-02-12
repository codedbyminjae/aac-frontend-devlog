package com.example.aac.data.mapper

import com.example.aac.data.remote.dto.WordResponse
import com.example.aac.domain.model.Word

object WordMapper {
    fun mapToDomain(response: WordResponse): List<Word> {
        return response.data.words
    }
}