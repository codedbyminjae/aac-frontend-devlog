package com.example.aac.data.mapper // ✅ 패키지 변경

import com.example.aac.R

object IconMapper {
    // 1. UI(Int) -> 서버(String) 변환 (저장할 때 사용)
    fun toRemoteKey(resId: Int): String {
        return when (resId) {
            R.drawable.ic_human -> "ic_human"
            R.drawable.ic_place -> "ic_place"
            R.drawable.ic_food -> "ic_food"
            R.drawable.ic_emotion -> "ic_emotion"
            R.drawable.ic_act -> "ic_act"
            R.drawable.ic_soccer -> "ic_soccer"
            R.drawable.ic_book -> "ic_book"
            R.drawable.ic_hand -> "ic_hand"
            R.drawable.ic_hospital -> "ic_hospital"
            R.drawable.ic_pill -> "ic_pill"
            R.drawable.ic_school -> "ic_school"
            R.drawable.ic_song -> "ic_song"
            R.drawable.ic_paint -> "ic_paint"
            R.drawable.ic_upload -> "ic_upload"
            else -> "ic_default"
        }
    }

    // 2. 서버(String) -> UI(Int) 변환 (불러올 때 사용)
    fun toLocalResource(key: String?): Int {
        return when (key) {
            "ic_human" -> R.drawable.ic_human
            "ic_place" -> R.drawable.ic_place
            "ic_food" -> R.drawable.ic_food
            "ic_emotion" -> R.drawable.ic_emotion
            "ic_act" -> R.drawable.ic_act
            "ic_soccer" -> R.drawable.ic_soccer
            "ic_book" -> R.drawable.ic_book
            "ic_hand" -> R.drawable.ic_hand
            "ic_hospital" -> R.drawable.ic_hospital
            "ic_pill" -> R.drawable.ic_pill
            "ic_school" -> R.drawable.ic_school
            "ic_song" -> R.drawable.ic_song
            "ic_paint" -> R.drawable.ic_paint
            "ic_upload" -> R.drawable.ic_upload
            else -> R.drawable.ic_default
        }
    }
}