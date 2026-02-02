package com.example.aac.ui.features.auto_sentence.repeat

data class RepeatSetting(
    val type: RepeatType,
    val days: Set<Weekday> = emptySet(),   // 매주 / 격주
    val monthDays: Set<Int> = emptySet()   // 매월 (나중)
)
