package com.example.aac.ui.features.auto_sentence.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeekdayRow(
    selectedDays: Set<Weekday>,
    onDayToggle: (Weekday) -> Unit
) {
    Row(
        modifier = Modifier.width(334.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Weekday.values().forEach { day ->
            WeekdayChip(
                weekday = day,
                selected = selectedDays.contains(day),
                onClick = { onDayToggle(day) }
            )
        }
    }
}
