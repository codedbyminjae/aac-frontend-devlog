package com.example.aac.core.navigation

import android.net.Uri

object Routes {

    // 로그인 화면
    const val LOGIN = "login"

    // 메인 화면
    const val MAIN = "main"

    // AI 문장 완성
    const val AI_SENTENCE = "ai_sentence"

    // AI 문장 편집
    const val AI_SENTENCE_EDIT = "ai_sentence_edit"
    const val AI_SENTENCE_EDIT_ROUTE = "$AI_SENTENCE_EDIT?text={text}"

    fun aiSentenceEditRoute(text: String): String =
        "$AI_SENTENCE_EDIT?text=${Uri.encode(text)}"

    // 설정 화면 (추가)
    const val SETTINGS = "settings"
}
