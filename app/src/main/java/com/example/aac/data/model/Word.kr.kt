package com.example.aac.data.model

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName

data class WordResponse(
    val success: Boolean,
    val data: WordData,
    val message: String
)

data class WordData(
    val category: String? = null,
    val words: List<Word>
)

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

enum class PartOfSpeech(val value: String) {
    NOUN("NOUN"),
    VERB("VERB"),
    ADJECTIVE("ADJECTIVE"),
    MODIFIER("MODIFIER"),
    EMOTION("EMOTION"),
    NONE("NONE");

    companion object {
        fun from(value: String): PartOfSpeech = entries.find { it.value == value } ?: NONE
    }
}

// 품사에 따른 색상 반환 확장 함수
fun PartOfSpeech.getBackgroundColor(): Color {
    return when (this) {
        PartOfSpeech.NOUN -> Color(0xFFFFE099)       // 명사: 노랑
        PartOfSpeech.VERB -> Color(0xFFC2ECC9)       // 동사: 초록
        PartOfSpeech.ADJECTIVE -> Color(0xFFCCE0FF)  // 형용사: 파랑
        PartOfSpeech.MODIFIER,
        PartOfSpeech.EMOTION -> Color(0xFFF0C2FF)    // 수식, 감정: 보라
        PartOfSpeech.NONE -> Color(0xFFFBFBF8)             // 기타: 흰색
    }
}

data class SpeakSettingCardUiModel(
    val id: String,
    val text: String,
    val imageUrl: String,
    val backgroundColor: Color
)

fun Word.toUiModel(): SpeakSettingCardUiModel {
    val pos = PartOfSpeech.from(this.partOfSpeech)
    return SpeakSettingCardUiModel(
        id = this.cardId,
        text = this.word,
        imageUrl = this.imageUrl,
        backgroundColor = pos.getBackgroundColor()
    )
}