package com.example.aac.ui.features.auto_sentence.repeat

enum class RepeatType(val label: String) {
    DAILY("매일"),
    WEEKLY("매주"),
    BIWEEKLY("격주"),
    MONTHLY("매월");

    companion object {
        val DEFAULT = WEEKLY
    }
}
