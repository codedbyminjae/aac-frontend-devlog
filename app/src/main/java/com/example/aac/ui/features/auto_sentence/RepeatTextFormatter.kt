package com.example.aac.ui.features.auto_sentence

import com.example.aac.ui.features.auto_sentence.repeat.*

fun RepeatSetting.toKoreanText(): String {
    return when (type) {
        RepeatType.DAILY -> "매일"

        RepeatType.WEEKLY ->
            "매주 " + days
                .toList()
                .sortedBy { it.ordinal }
                .joinToString(", ") { it.label }

        RepeatType.BIWEEKLY ->
            "격주 " + days
                .toList()
                .sortedBy { it.ordinal }
                .joinToString(", ") { it.label }

        RepeatType.MONTHLY ->
            if (monthDays.isEmpty()) {
                "매월"
            } else {
                "매월 " + monthDays
                    .toList()
                    .sorted()
                    .joinToString(", ") + "일"
            }
    }
}
