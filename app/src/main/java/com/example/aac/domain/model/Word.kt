package com.example.aac.domain.model
data class Word(
    val cardId: String,
    val categoryId: String,
    val partOfSpeech: String,
    val word: String,
    val imageUrl: String,
    val isDefault: Boolean,
    val isFavorite: Boolean,
    val displayOrder: Int
)