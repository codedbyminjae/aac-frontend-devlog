package com.example.aac.data.remote.dto
import com.example.aac.domain.model.Word

data class WordResponse(
    val success: Boolean,
    val data: WordData,
    val message: String
)

data class WordData(
    val words: List<Word>
)